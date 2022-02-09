import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

	DTNode rootDTNode;
	int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split
	
	// Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
	public static final long serialVersionUID = 343L;
	
	public DecisionTree(ArrayList<Datum> datalist , int min) {
		minSizeDatalist = min;
		rootDTNode = (new DTNode()).fillDTNode(datalist);
	}

	class DTNode implements Serializable{
		//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
		public static final long serialVersionUID = 438L;
		boolean leaf;
		int label = -1;      // only defined if node is a leaf
		int attribute; // only defined if node is not a leaf
		double threshold;  // only defined if node is not a leaf

		DTNode left, right; //the left and right child of a particular node. (null if leaf)

		DTNode() {
			leaf = true;
			threshold = Double.MAX_VALUE;
		}

		
		// this method takes in a datalist (ArrayList of type datum). It returns the calling DTNode object 
		// as the root of a decision tree trained using the datapoints present in the datalist variable and minSizeDatalist.
		// Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
		DTNode fillDTNode(ArrayList<Datum> datalist) {

			//ADD CODE HERE
			// if the labelled data set has at least k data items
			if  (datalist.size() >= minSizeDatalist)
			{
				int [] countsLabels = new int[2];
				//loop through the data and count the occurrences of datapoints of each label
				for (Datum data : datalist)
				{
					countsLabels[data.y]+=1;
				}
				// if all the datapoints are of the label 0
				if (countsLabels[0] == datalist.size())
				{
					this.leaf = true;
					this.label = 0;
					return this;
				}

				// if all the datapoints are of the label 1
				else if (countsLabels[1] == datalist.size())
				{
					this.leaf = true;
					this.label = 1;
					return this;
				}


				// create a best attribute test question
				else
				{
					// double best_avg_entropy = Double.POSITIVE_INFINITY;
					double best_avg_entropy = calcEntropy(datalist);
					int best_attribute = -1;
					double best_threshold = -1;
					int datasetSize = datalist.size();
					// for each attribute in double[] x
					for (int i = 0; i < datalist.get(0).x.length; i++)
					{
						// for each data point in list
						for (Datum data : datalist)
						{
							// Compute split and current_avg_entropy based on xi of current data point
							int countLabel0_lessThan_xi = 0;
							int countLabel1_lessThan_xi = 0;
							int countLabel0_greaterThan_xi = 0;
							int countLabel1_greaterThan_xi = 0;
							double currentThreshold = data.x[i];
							double current_avg_entropy = calcEntropy(datalist);

							for (Datum eachForSplit : datalist)
							{
								// split the dataset according to xi, this is the less than xi case
								if (eachForSplit.x[i] < currentThreshold)
								{
									if (eachForSplit.y == 0)
									{
										countLabel0_lessThan_xi += 1;
									}
									else
									{
										countLabel1_lessThan_xi += 1;
									}
								}
								// split the dataset according to xi, this is the greater than or equal to xi case
								else
								{
									if (eachForSplit.y == 0)
									{
										countLabel0_greaterThan_xi += 1;
									}
									else
									{
										countLabel1_greaterThan_xi += 1;
									}
								}
							}

							int numberOfLeft = countLabel0_lessThan_xi + countLabel1_lessThan_xi;
							int numberOfRight = countLabel0_greaterThan_xi + countLabel1_greaterThan_xi;
							double entropy_left_label0;
							double entropy_left_label1;
							double entropy_right_label0;
							double entropy_right_label1;

							// if the current threshold could not divide the dataset into two parts, no need to update values
							if (numberOfLeft == 0 || numberOfRight == 0)
							{
							 	// best_avg_entropy = current_avg_entropy;
								continue;
							}

							else
							{
								double weight_left = (double)numberOfLeft / datasetSize;
								double weight_right = (double)numberOfRight / datasetSize;
								double p_left_label0 = Math.max((double)countLabel0_lessThan_xi / numberOfLeft, 0.00000001);
								double p_left_label1 = Math.max((double)countLabel1_lessThan_xi / numberOfLeft, 0.00000001);
								double p_right_label0 = Math.max((double)countLabel0_greaterThan_xi / numberOfRight, 0.00000001);
								double p_right_label1 = Math.max((double)countLabel1_greaterThan_xi / numberOfRight, 0.00000001);
								entropy_left_label0 = p_left_label0 * (Math.log(p_left_label0) / Math.log(2));
								entropy_left_label1 = p_left_label1 * (Math.log(p_left_label1) / Math.log(2));
								entropy_right_label0 = p_right_label0 * (Math.log(p_right_label0) / Math.log(2));
								entropy_right_label1 = p_right_label1 * (Math.log(p_right_label1) / Math.log(2));

								current_avg_entropy = weight_left * (- (entropy_left_label0 + entropy_left_label1)) + 
											weight_right * (- (entropy_right_label0 + entropy_right_label1));

								if (current_avg_entropy < best_avg_entropy)
								{
									best_avg_entropy = current_avg_entropy;
									best_attribute = i;
									best_threshold = currentThreshold;
								}
							}

						}
					}

					// if the attribute can not divide the dataset into two subsets
					if (best_attribute == -1)
					{
						this.leaf = true;
						this.label = findMajority(datalist);
						return this;
					}

					// Now we have the best attribute test question -- best_attribute and best_threshold
					// Create a new node and store the attribute test in that node
					else
					{
						this.leaf = false;
						this.attribute = best_attribute;
						this.threshold = best_threshold;

						// split the set of data items into two subsets, datalist_left and datalist_right, 
						// according to the answers to the test question
						ArrayList<Datum> datalist_left = new ArrayList<>();
						ArrayList<Datum> datalist_right = new ArrayList<>();
						for (int i = 0; i < datalist.size(); i++)
						{
							if (datalist.get(i).x[best_attribute] < best_threshold)
							{
								datalist_left.add(datalist.get(i));
							}

							else
							{
								datalist_right.add(datalist.get(i));
							}
						}

						// recursively call the method
						this.left = new DTNode().fillDTNode(datalist_left);
						this.right = new DTNode().fillDTNode(datalist_right);
						return this;
					}
					
				}
			}

			// if the datalist size is less than the minSizeDatalist
			// When the size of the dataset falls below the minimum size (minSizeDatalist)
			// but still contains datapoints from more than one class
			// so create a leaf node, use the majority label
			else 
			{
				this.leaf = true;
				this.label = findMajority(datalist);
				return this;
			}		
			
		}



		// This is a helper method. Given a datalist, this method returns the label that has the most
		// occurrences. 
		// In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
		int findMajority(ArrayList<Datum> datalist) {
			
			int [] votes = new int[2];

			//loop through the data and count the occurrences of datapoints of each label
			for (Datum data : datalist)
			{
				votes[data.y]+=1;
			}
			
			if (votes[0] >= votes[1])
				return 0;
			else
				return 1;
		}




		// This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
		// returns its corresponding label, as determined by the decision tree
		int classifyAtNode(double[] xQuery) {
			
			//ADD CODE HERE
			// if the node itself is a leaf
			if (this.leaf)
			{
				return this.label;
			}

			// If the node is not a leaf
			else
			{
				if (xQuery[this.attribute] < this.threshold)
				{
					// this = this.left;
					return this.left.classifyAtNode(xQuery);
				}
				else
				{
					return this.right.classifyAtNode(xQuery);
				}
			}


			// return -1; //dummy code.  Update while completing the assignment.
		}


		//given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
		//at DTNode object passed as the parameter
		public boolean equals(Object dt2)
		{

			//ADD CODE HERE
			// if same reference, return true
			if (dt2 == this)
			{
				return true;
			}

			// false if dt2 is of different class
			else if (dt2 == null || this.getClass() != dt2.getClass())
			{
				return false;
			}

			// same class
			else
			{
				DTNode dtnode2 = (DTNode)dt2;
				// whether leaf?
				if (this.leaf == true && dtnode2.leaf == true)
				{
					// if both are leaf, compare the label
					if (this.label == dtnode2.label)
					{
						return true;
					}
					else
					{
						return false;
					}			
				}
				// one is leaf, another is not leaf, return false
				else if (this.leaf != dtnode2.leaf)
				{
					return false;
				}
				// if they are not leaf
				else 
				{
					if (this.attribute!=dtnode2.attribute || this.threshold != dtnode2.threshold)
					{
						//if (this.left.equals(dtnode2.left) && this.right.equals(dtnode2.right))
						return false;
					}
					else if (!(this.left.equals(dtnode2.left)))
					{
						return false;
					}
					else if (!(this.right.equals(dtnode2.right)))
					{
						return false;
					}
					else
					{
						return true;
					}
				}
			}
				
			}
			// return false; //dummy code.  Update while completing the assignment.
		}
	



	//Given a dataset, this returns the entropy of the dataset
	double calcEntropy(ArrayList<Datum> datalist) {
		double entropy = 0;
		double px = 0;
		float [] counter= new float[2];
		if (datalist.size()==0)
			return 0;
		double num0 = 0.00000001,num1 = 0.000000001;

		//calculates the number of points belonging to each of the labels
		for (Datum d : datalist)
		{
			counter[d.y]+=1;
		}
		//calculates the entropy using the formula specified in the document
		for (int i = 0 ; i< counter.length ; i++)
		{
			if (counter[i]>0)
			{
				px = counter[i]/datalist.size();
				entropy -= (px*Math.log(px)/Math.log(2));
			}
		}

		return entropy;
	}


	// given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
	int classify(double[] xQuery ) {
		return this.rootDTNode.classifyAtNode( xQuery );
	}

	// Checks the performance of a DecisionTree on a dataset
	// This method is provided in case you would like to compare your
	// results with the reference values provided in the PDF in the Data
	// section of the PDF
	String checkPerformance( ArrayList<Datum> datalist) {
		DecimalFormat df = new DecimalFormat("0.000");
		float total = datalist.size();
		float count = 0;

		for (int s = 0 ; s < datalist.size() ; s++) {
			double[] x = datalist.get(s).x;
			int result = datalist.get(s).y;
			if (classify(x) != result) {
				count = count + 1;
			}
		}

		return df.format((count/total));
	}


	//Given two DecisionTree objects, this method checks if both the trees are equal by
	//calling onto the DTNode.equals() method
	public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
	{
		boolean flag = true;
		flag = dt1.rootDTNode.equals(dt2.rootDTNode);
		return flag;
	}

}
