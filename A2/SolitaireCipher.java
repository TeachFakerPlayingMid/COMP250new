package assignment2;


import java.util.Arrays;
import java.util.Locale;

public class SolitaireCipher {
	public Deck key;
	
	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}
	
	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size)
	{
		/**** ADD CODE HERE ****/
		int[] KeyStreamArray = new int[size];
		for (int i=0;i<size;i++)
		{
			KeyStreamArray[i]=key.generateNextKeystreamValue();
		}
		return KeyStreamArray;
	}
		
	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg)
	{
		/**** ADD CODE HERE ****/
		msg = msg.toUpperCase(Locale.ROOT);
		String temp = null;
		//String encode =null;
		char[] encodeArray1 = new char[msg.length()];

		for (int j=0;j<encodeArray1.length;j++)
		{
			if ((encodeArray1[j]>65&&encodeArray1[j]<90)||(encodeArray1[j]>97&&encodeArray1[j]<122))
			{
				temp += msg.charAt(j);
			}
		}
		char[] encodeArray2 = new char[msg.length()];
		char[] encodeArray3 = new char[msg.length()];
		int newIndex=0;
		for (int i=0;i<encodeArray2.length;i++)
		{
			encodeArray2[i] = msg.charAt(i);
		}
		int[] KeyStreamArray = getKeystream(msg.length());
		int diff = 0;
		for (int i=0;i<KeyStreamArray.length;i++)
		{
			encodeArray3[i] = (char)((int)encodeArray2[i] +KeyStreamArray[i]);
		}
		return Arrays.toString(encodeArray3);
	}
	
	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg)
	{
		/**** ADD CODE HERE ****/
		int[] KeyStreamArray = getKeystream(msg.length());
		char[] decodeArray1 = new char[msg.length()];
		for (int i = 0;i<msg.length();i++)
		{
			decodeArray1[i]=msg.charAt(i);
		}
		char[] decodeArray2 = new char[msg.length()];
		for (int i=0;i<KeyStreamArray.length;i++)
		{
			decodeArray2[i] = (char)((int)decodeArray1[i]-KeyStreamArray[i]);
		}
		StringBuilder str = new StringBuilder();
		for (char c :decodeArray2)
		{
			str.append(c);
		}

		return str.toString();
	}
	
}
