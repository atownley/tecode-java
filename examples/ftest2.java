import java.util.*;
import com.townleyenterprises.filter.*;

class Widget
{
	Widget(String s, int i)
	{
		sval = s;
		ival = i;
	}

	public String getSval()
	{
		return sval;
	}
	
	public int getIval()
	{
		return ival;
	}

	public String toString()
	{
		return "sval = '" + sval + "'; ival = " + ival;
	}

	final String sval;
	final int ival;
}

class Contains implements Filter
{
	Contains(String s)
	{
		tval = s;
	}

	public boolean doFilter(Object o)
	{
		if(!(o instanceof Widget))
			return false;

		String s = ((Widget)o).getSval();
		if(s.indexOf(tval) != -1)
		{
			return true;
		}
		return false;
	}

	final String tval;
}

class LongerThan implements Filter
{
	LongerThan(int l)
	{
		length = l;
	}

	public boolean doFilter(Object o)
	{
		if(!(o instanceof Widget))
			return false;

		String s = ((Widget)o).getSval();
		if(s.length() > length)
		{
			return true;
		}
		return false;
	}

	final int length;
}

class ftest2
{
	public static void main(String[] args)
	{
		ArrayList l = new ArrayList();
		l.add(new Widget("filter", 100));
		l.add(new Widget("this should be filtered", 357));
		l.add(new Widget("some string", 7));
		l.add(new Widget("whee", 20));
		l.add(new Widget("fil", 20));
		l.add(new Widget("plants are fun", 4));
		l.add(new Widget("filtered water is good", 25));

		LogicalAndFilter and = new LogicalAndFilter();
		and.addFilter(new Contains("fil"));
		and.addFilter(new LongerThan(5));

		FilteredIterator i;
		i = new FilteredIterator(l.iterator(), and);
	
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
	}
}
