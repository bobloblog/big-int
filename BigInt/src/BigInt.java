import java.util.ArrayList;
@SuppressWarnings("rawtypes")

public class BigInt implements Comparable
{
	private ArrayList<Integer> num;
	private boolean negative = false;
	
	public BigInt(int integer)
	{
		this(((Integer)integer).toString());
	}
	
	public BigInt(String integer)
	{
		num = new ArrayList<Integer>();
		
		if(integer.substring(0, 1).equals("-"))
		{
			integer = integer.substring(1);
			negative = true;
		}
		
		for(int i = integer.length() - 1; i >= 0; i--)
			num.add(Integer.parseInt(integer.substring(i, i + 1)));
	}

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
		else if(isNegative() && !addend.isNegative())
		{
			if(num.size() < addend.size())
				return new BigInt("-" + reverseString(addRecursive(0, this.toArray(), addend.toArray())));
			else
				return new BigInt("-" + reverseString(addRecursive(0, addend.toArray(), this.toArray())));
		}
		
		return null;
	}
	
	private String addRecursive(int slot, ArrayList<Integer> augend, ArrayList<Integer> addend)
	{
		System.out.println("start");
		if(slot >= augend.size())
		{
			System.out.println("end");
			String temp = "";
			for(int i = slot; i < addend.size(); i++)
				temp+=addend.get(i);
			return temp;
		}
		
		System.out.print("adds to ");
		Integer digit = (augend.get(slot) + addend.get(slot));
		System.out.println(digit);
		
		if(digit < 10)
		{
			System.out.println("less than ten");
			return digit.toString() + addRecursive(slot += 1, augend, addend);
		}
		else
		{
			System.out.println("greater than ten");
			try{addend.set(slot + 1, addend.get(slot + 1) + 1);}
			catch(Exception e){return ((Integer)(digit % 10)).toString() + "1";}
			return ((Integer)(digit % 10)).toString() + addRecursive(slot += 1, augend, addend);
		}
	}
	
	private String reverseString(String integer)
	{
		if(integer.substring(0, 1).equals("-"))
		{
			integer = integer.substring(1);
			return ("-" + new StringBuffer(integer).reverse().toString());
		}
		return (new StringBuffer(integer).reverse().toString());
	}
	
	public boolean isNegative()
	{
		return negative;
	}
	
	public String toString()
	{
		String temp = "";
		if(negative)
			temp += "-";
		for(int i = num.size() - 1; i >= 0 ; i--)
			temp += num.get(i);
		return temp;
	}
	
	public ArrayList<Integer> toArray()
	{
		return num;
	}
	
	public void print()
	{
		if(negative)
			System.out.print("-");
		for(int i = num.size() - 1; i >= 0 ; i--)
			System.out.print(num.get(i));
	}
	
	public int size()
	{
		return num.size();
	}

	
	@Override
	public int compareTo(Object arg0)
	{
		return this.toString().compareTo(arg0.toString());
	}
}
