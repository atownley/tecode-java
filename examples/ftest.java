import java.util.*;
import com.townleyenterprises.filter.*;

class ftest
{
	public static void main(String[] args)
	{
		ArrayList l = new ArrayList();
		l.add("filter");
		l.add("this string should be filtered");
		l.add("some string");
		l.add("whee");
		l.add("plants are fun");
		l.add("filtered water is good");

		FilteredIterator i = new FilteredIterator(l.iterator(),
			new Filter() {
				public boolean doFilter(Object o)
				{
					String s = (String)o;
					if(s.indexOf("filter") != -1)
					{
						return true;
					}
					return false;
				}
			});
	
		while(i.hasNext())
		{
			System.out.println(i.next());
		}
	}
}
