import java.util.ArrayList;

//It works... most of the time. =|
public class BigInt
{
	private ArrayList<Integer> num;
	private boolean negative = false;
	
	/**
	 * Creates a new BigInt when given an int
	 * @param integer An int value
	 */
	public BigInt(int integer)
	{
		this(((Integer)integer).toString());
	}
	
	/**
	 * Creates a new BigInt when given a String
	 * @param integer A string representing the integer.  Can include a negative.
	 */
	public BigInt(String integer)
	{
		num = new ArrayList<Integer>();
		
		if(integer.substring(0, 1).equals("-"))
		{
			integer = integer.substring(1);
			negative = true;
		}
		
		integer = parseZeros(integer);
		
		for(int i = integer.length() - 1; i >= 0; i--)
		{
			try{num.add(Integer.parseInt(integer.substring(i, i + 1)));}
			catch(Exception e)
			{
				System.out.println("Invalid string. Setting value to zero.");
				num = new ArrayList<Integer>(); num.add(0);
				break;
			}
		}
	}

	/**
	 * Adds the given int value to this BigInt
	 * @param addend An int value to be added
	 * @return A new BigInt with the new value
	 */
	public BigInt add(int addend)
	{
		return add(new BigInt(addend));
	}
	
	/**
	 * Adds the given BigInt to this BigInt
	 * @param addend A BigInt value to be added
	 * @return A BigInt representing the sum
	 */
	public BigInt add(BigInt addend)
	{		
		if(!isNegative() && !addend.isNegative())
		{
			if(num.size() < addend.size())
				return new BigInt(reverseString(addRecursive(0, this.toArray(), addend.toArray())));
			else
				return new BigInt(reverseString(addRecursive(0, addend.toArray(), this.toArray())));
		}
		else if(isNegative() && addend.isNegative())
		{
			if(num.size() < addend.size())
				return new BigInt("-" + reverseString(addRecursive(0, this.toArray(), addend.toArray())));
			else
				return new BigInt("-" + reverseString(addRecursive(0, addend.toArray(), this.toArray())));
		}
		else
			return this.subtract(addend);
	}
	
	/**
	 * The recursive method for adding
	 * @param slot The current slot in the arrays; begin with 0
	 * @param augend The number with fewer digits
	 * @param addend The number with more digits
	 * @return A reversed string of the sum
	 */
	private String addRecursive(int slot, ArrayList<Integer> augend, ArrayList<Integer> addend)
	{
		if(slot >= augend.size())
		{
			String temp = "";
			for(int i = slot; i < addend.size(); i++)
				temp+=addend.get(i);
			return temp;
		}
		
		Integer digit = (augend.get(slot) + addend.get(slot));
		
		if(digit < 10)
			return digit.toString() + addRecursive(slot += 1, augend, addend);
		else
		{
			try{addend.set(slot + 1, addend.get(slot + 1) + 1);}
			catch(Exception e){return ((Integer)(digit % 10)).toString() + "1";}
			return ((Integer)(digit % 10)).toString() + addRecursive(slot += 1, augend, addend);
		}
	}
	
	/**
	 * Subtracts the given int value from this BigInt
	 * @param subtrahend An int value to be subtracted
	 * @return A new BigInt representing the answer
	 */
	public BigInt subtract(int subtrahend)
	{
		return subtract(new BigInt(subtrahend));
	}
	
	/**
	 * Subtracts the given BigInt value from this BigInt
	 * @param subtrahend A BigInt value to be subtracted
	 * @return A new BigInt representing the answer
	 */
	public BigInt subtract(BigInt subtrahend)
	{
		System.out.println("Start subtraction");
		if(isNegative() && !subtrahend.isNegative())
		{
			System.out.println("one");
			subtrahend.setNegative(true);
			return add(subtrahend);
		}
		else if(!isNegative() && subtrahend.isNegative())
		{
			System.out.println("two");
			subtrahend.setNegative(false);
			return this.add(subtrahend);
		}
		else if(!isNegative() && !subtrahend.isNegative())
		{
			if(this.compareTo(subtrahend) == 0)
				return new BigInt(0);
			System.out.println("three");
			BigInt temp = (this.add(subtrahend.compliment()));
			if(this.compareTo(subtrahend) < 0)
			{
				temp = temp.compliment();
				temp.setNegative(true);
				return temp;
			}
			if(temp.toString().length() > 2)
				try{temp = new BigInt(temp.toString().substring(1));}
				catch(Exception e){}
			return temp.add(1);
		}
		else
		{
			System.out.println("four;" + this + ":" + subtrahend);
			if(this.compareTo(subtrahend) == 0)
				return new BigInt(0);
			else if(this.compareTo(subtrahend) < 0)
			{
				BigInt temp = this.compliment();
				temp = (temp.add(subtrahend));
				try{temp = new BigInt(temp.toString().substring(1));}
				catch(Exception e){}
				
				temp = temp.add(1);
				return temp;
			}
			else
			{
				System.out.println("in correct statement");
				BigInt temp = this.compliment();
				temp = (temp.add(subtrahend));
				
				try{temp = new BigInt(temp.toString().substring(1));}
				catch(Exception e){}
				
				temp = temp.compliment();
				temp.setNegative(true);
				return temp;
			}

		}
	}
	
	/**
	 * Multiplies the given int value with this BigInt
	 * @param multiplier An integer to be multiplied
	 * @return A new BigInt representing the product
	 */
	public BigInt multiply(int multiplier)
	{
		return multiply(new BigInt(multiplier));
	}
	
	/**
	 * Multiplies the given BigInt value with this BigInt
	 * @param multiplier A BigInt to be multiplied
	 * @return A new BigInt representing the product
	 */
	public BigInt multiply(BigInt multiplier)
	{
		ArrayList<Integer> mult = multiplier.toArray();
		BigInt product = new BigInt(0);
		String temp = "";
		int addToNext = 0;

		if(num.size() < mult.size())
			for(int i = 0; i < this.size(); i++)
			{
				temp = "";
				for(int k = 0; k < i; k++)
					temp += "0";
				for(int j = 0; j < mult.size(); j++)
				{
					temp += (((Integer)(((mult.get(j) * num.get(i)) % 10) + addToNext)).toString());
					addToNext = (mult.get(j) * num.get(i)) / 10;
				}
					product.add(new BigInt(reverseString(temp + ((Integer)addToNext).toString())));
			}						
		else
			for(int i = 0; i < mult.size(); i++)
			{
				temp = "";
				for(int k = 0; k < i; k++)
					temp += "0";
				for(int j = 0; j < num.size(); j++)
				{
					temp += (((Integer)(((num.get(j) * mult.get(i)) % 10) + addToNext)).toString());
					addToNext = (num.get(j) * mult.get(i)) / 10;
				}
				if(addToNext > 0)
					temp += ((Integer)addToNext).toString();
				product = product.add(new BigInt(reverseString(temp)));
			}	
		if((!isNegative() && !multiplier.isNegative()) ||(isNegative() && multiplier.isNegative()))
			return product;
		else
			return new BigInt("-" + product.toString());
		}
	
	/**
	 * Parses the zeros from the front of a string representation of a BigInt; ie 000123
	 * @param integer An integer that is represented by a string; ie "000123"
	 * @return A string representing the parsed integer; ie "123" 
	 */
	private String parseZeros(String integer)
	{
		for(int i = 0; i < integer.length(); i++)
			if(integer.charAt(i) != '0')
				return integer.substring(i);
		return integer;
	}
	
	/**
	 * Reverses a string representation of an integer; BigInt uses a reversed array list to represent the integer; ie "321" represents "123"
	 * @param integer A string representing an integer (or reversed integer); ie "321"
	 * @return A string representation of the reversed (or corrected) integer; ie "123"
	 */
	private String reverseString(String integer)
	{
		if(integer.substring(0, 1).equals("-"))
		{
			integer = integer.substring(1);
			return ("-" + new StringBuffer(integer).reverse().toString());
		}
		return (new StringBuffer(integer).reverse().toString());
	}
	
	/**
	 * This determines the compliment of the integer following the Ten's Compliment rule; ie "123" returns "876"
	 * @return The compliment of the BigInt; ie "123" returns "876"
	 */
	public BigInt compliment()
	{
		String temp = "";
		for(int i = num.size() - 1; i >= 0; i--)
			temp += ( 9 - num.get(i));
		return new BigInt(temp);
	}
	
	/**
	 * Sets this BigInt to it's absolute value
	 */
	public void abs()
	{
		negative = false;
	}
	
	/**
	 * Determines if this BigInt is negative
	 * @return True if it is negative, false otherwise
	 */
	public boolean isNegative()
	{
		if(this.compareTo(0) == 0)
			negative = false;
		return negative;
	}
	
	/**
	 * Sets the sign of this BigInt
	 * @param b True to set it to negative, false to set it to positive
	 */
	public void setNegative(boolean b)
	{
		if(this.compareTo(0) == 0)
			negative = false;
		else
			negative = b;
	}
	
	/**
	 * Returns the string representation of this BigInt; ie 123 returns "123"
	 */
	public String toString()
	{
		String temp = "";
		if(negative)
			temp += "-";
		for(int i = num.size() - 1; i >= 0 ; i--)
			temp += num.get(i);
		return temp;
	}
	
	/**
	 * Provides an array of Integers representing the BigInt in reverse; ie "123" returns "3" "2" "1"
	 * @return An array of Integers representing the BigInt in reverse; ie "123" returns "3" "2" "1"
	 */
	public ArrayList<Integer> toArray()
	{
		return num;
	}
	
	/**
	 * Prints this BigInt. But seriously, you can just do this: System.out.println(new BigInt(123));
	 */
	public void print()
	{
		if(this.compareTo(0) == 0)
			negative = false;
		if(negative)
			System.out.print("-");
		for(int i = num.size() - 1; i >= 0 ; i--)
			System.out.print(num.get(i));
	}
	
	/**
	 * Determines the number of digits in this BigInt; ie "123" returns "3"
	 * @return An int representing the number of digits of this BigInt; ie "123" returns "3"
	 */
	public int size()
	{
		return num.size();
	}
	
	
	/**
	 * Compares this BigInt to another
	 * @param o Another BigInt
	 * @return What any other compareTo method returns
	 */
	public int compareTo(BigInt o)
	{
		if(toString().length() != o.toString().length())
			return toString().length() - o.toString().length();
			return toString().compareTo(o.toString());
	}
	
	/**
	 * Pretty obvious
	 * @param Do I really have to explain?
	 * @return Seriously?
	 */
	public int compareTo(int i)
	{
		return compareTo(new BigInt(i));
	}
}
