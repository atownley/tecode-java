import com.townleyenterprises.filter.*;
import com.townleyenterprises.persistence.*;
import java.util.HashMap;

class Foo
{
	public Foo() {}

	public String getString() { return "string"; }
	public int getInt() { return 100; }
	public Long getLong() { return new Long(1000); }
}

public class fatest
{
	public static void main(String[] args)
	{
		SortSpecification[] sort = {
			new SortSpecification("int", SortOrder.DESCENDING),
			new SortSpecification("string")
		};

		SortSpecification[] sortstr = {
			new SortSpecification("string", SortOrder.DESCENDING)
		};

		SortSpecification[] sortfa = {
			new SortSpecification("int")
		};
		LogicalAndFilter and = new LogicalAndFilter();
		LogicalOrFilter or = new LogicalOrFilter();
		QueryFilter qf = new QueryFilter(Foo.class,
					"string",
					QueryOperator.EQ,
					"string");

		SubstringFilter substr = new SubstringFilter(Foo.class,
					"string", "ring");
		and.addFilter(qf);
		and.addFilter(new QueryFilter(Foo.class,
					"int",
					QueryOperator.LT,
					new Integer(20)));
		and.addFilter(new QueryFilter(fatest.class,
					"int",
					QueryOperator.GT,
					new Integer(10)));
		or.addFilter(and);
		or.addFilter(new QueryFilter(Foo.class,
					"long",
					QueryOperator.GE,
					new Long(1000)));
		or.addFilter(new LogicalNotFilter(and));

		SQLFilterAdapter adapter = new SQLFilterAdapter(substr);
		System.out.println(adapter.toSQL(sortstr));
		System.out.println("Parameters:  " + 
			printArray(adapter.getQueryParameters()));
		adapter = new SQLFilterAdapter(qf);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));

		adapter = new SQLFilterAdapter(and);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));
		adapter = new SQLFilterAdapter(or);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));

		// try with the hashmap
		HashMap tables = new HashMap();
		tables.put(Foo.class, "fubar");
		System.out.println("\nTest table mapping Foo -> fubar");
		adapter = new SQLFilterAdapter(substr, tables);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " + 
			printArray(adapter.getQueryParameters()));
		adapter = new SQLFilterAdapter(qf, tables);
		System.out.println(adapter.toSQL(Foo.class, sort));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));

		// add a new table mapping
		tables.put(fatest.class, "program_file");
		System.out.println("\nTest table mapping added fatest -> program_file");
		adapter = new SQLFilterAdapter(and, tables);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));
		adapter = new SQLFilterAdapter(or, tables);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));
		
		// add a column mapping
		HashMap cols = new HashMap();
		HashMap foomap = new HashMap();
		foomap.put("int", "intcol");
		foomap.put("string", "stringcol");
		cols.put(Foo.class, foomap);

		// add the sort for both tables
		HashMap sortmap = new HashMap();
		sortmap.put(Foo.class, sort);
		sortmap.put(fatest.class, sortfa);

		System.out.println("\nTest column map");
		adapter = new SQLFilterAdapter(and, tables, cols);
		System.out.println(adapter.toSQL("foo, bar"));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));
		adapter = new SQLFilterAdapter(or, tables, cols);
		System.out.println(adapter.toSQL(sortmap));
		System.out.println("Parameters:  " +
			printArray(adapter.getQueryParameters()));
	}

	static String printArray(Object[] array)
	{
		StringBuffer buf = new StringBuffer("( ");
		for(int i = 0; i < array.length; ++i)
		{
			buf.append(array[i].toString());
			if(i < array.length - 1)
				buf.append(", ");
		}
		buf.append(" )");
		return buf.toString();
	}
}
