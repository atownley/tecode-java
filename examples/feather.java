import java.util.*;
import com.townleyenterprises.command.*;

class RepeatableOption extends CommandOption
{
	RepeatableOption(String longName, char shortName,
			String argHelp, String argDesc)
	{
		super(longName, shortName, true, argHelp, argDesc);
	}

	public void optionMatched(String arg)
	{
		super.optionMatched(arg);
		_args.add(arg);
	}

	List getArgs()
	{
		return _args;
	}

	ArrayList	_args = new ArrayList();
}


public class feather implements CommandListener
{
	static CommandOption create = new CommandOption("create", 'c',
			false, null, "Create a new archive.");
	static CommandOption file = new CommandOption("file", 'f',
			true, "<filename>",
			"Specify the name of the archive"
				+ " (default is stdout).");
	static RepeatableOption xclude = new RepeatableOption("exclude",
			'X', "[ <filename> | <directory> ]",
			"Exclude the named file or directory"
				+ " from the archive");
	
	static	CommandOption[] opts = { create, file, xclude };

	public CommandOption[] getOptions()
	{
		return opts;
	}

	public void optionMatched(CommandOption opt, String arg)
	{
		// nothing to do
	}

	public String getDescription()
	{
		return "feather options";
	}

	public static void main(String[] args)
	{
		CommandParser clp = new CommandParser("feather", "FILE...");
		clp.addCommandListener(new feather());
		clp.parse(args);

		if((file.getMatched() || xclude.getMatched()) &&
				!create.getMatched())
		{
			System.err.println("error:  nothing to do");
			clp.usage();
			System.exit(-1);
		}

		String[] largs = clp.getUnhandledArguments();
		if(create.getMatched() && largs.length == 0)
		{
			System.err.println("error:  refusing to create empty archive.");
			clp.usage();
			System.exit(-2);
		}
		
		for(int i = 0; i < largs.length; ++i)
		{
			System.out.println("largs[" + i + "] = '" +
				largs[i] + "'");
		}

		if(xclude.getMatched())
		{
			System.out.println("Excluded:");

			List xas = xclude.getArgs();
			for(Iterator j = xas.iterator(); j.hasNext();)
			{
				System.out.println(j.next());
			}
		}
	}
}
