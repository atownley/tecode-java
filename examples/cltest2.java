import java.util.*;
import com.townleyenterprises.command.*;

public class cltest2 implements CommandListener
{
	static DelimitedCommandOption	reallylong = new DelimitedCommandOption("really-long", 'r', "<param>,<param2>,<param3>,<param4>,<param5>", "this option has a really, really, long description that will wrap accross more that two lines so that we can see of the bug with multi-line text wrapping has been fixed.  The quick brown fox jumped over the lazy dog.  I'm a pepper, he's a pepper, she's a pepper.  Wouldn't you like to be a pepper too?");

	static	CommandOption[] opts = { reallylong };

	public CommandOption[] getOptions() { return opts; }
	public void optionMatched(CommandOption opt, String arg) { }

	public String getDescription()
	{
		return "cltest2 options";
	}

	public static void main(String[] args)
	{
		CommandParser clp = new CommandParser("cltest2");
		clp.addCommandListener(new cltest2());
		clp.setExitOnMissingArg(true, -100);
		clp.parse(args);

		if(reallylong.getMatched())
		{
			System.out.println("really-long values:");
			List l = reallylong.getArgs();
			for(Iterator it = l.iterator(); it.hasNext();)
			{
				System.out.println(it.next());
			}
		}

		String[] largs = clp.getUnhandledArguments();
		for(int i = 0; i < largs.length; ++i)
		{
			System.out.println("largs[" + i + "] = '" +
				largs[i] + "'");
		}
	}
}
