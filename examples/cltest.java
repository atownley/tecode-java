import com.townleyenterprises.command.*;

public class cltest implements CommandListener
{
	static CommandOption		toggle = new CommandOption("toggle", (char)0, false, null, "This option can be used to test if it was matched or not.  It doesn't do anything else.");
	static CommandOption		oomatch = new CommandOption("oomatch", (char)0, true, "<arg>", "I can handle myself!  Aren't you proud of me?") {
		public void optionMatched(String arg)
		{
			System.out.println("You matched me!");
			System.out.println("My argument was:  '" + arg + "'");
		}
	};

	static	CommandOption[] opts = {
		new CommandOption("foo", 'f', false, null, "isn't this foo?"),
		new CommandOption("long", 'l', true, "<param>", "this option has a really, really, long description that will wrap"),
		new CommandOption("really-long", 'r', true, "<param>,<param2>,<param3>,<param4>,<param5>", "this option has a really, really, long description that will wrap accross more that two lines so that we can see of the bug with multi-line text wrapping has been fixed.  The quick brown fox jumped over the lazy dog.  I'm a pepper, he's a pepper, she's a pepper.  Wouldn't you like to be a pepper too?"),
		new CommandOption("bar", 'b', true, "name", "specifies the bar"),
		new CommandOption("onlylong", (char)0, true, "<whee>", "check to see how to handle the option"),
		new CommandOption(null, 'X', true, "<super secret option>", "Only short options are supported as well"),
		toggle,
		oomatch
	};

	public CommandOption[] getOptions()
	{
		return opts;
	}

	public void optionMatched(CommandOption opt, String arg)
	{
		switch(opt.getShortName().charValue())
		{
			case 'f':
				System.out.println("matched f");
				break;
			case 'b':
				System.out.println("matched bar:  " + arg);
				break;
			case 'X':
				System.out.println("matched X:  " + arg);
				break;
			default:
				if("onlylong".equals(opt.getLongName()))
				{
					System.out.println("matched onlylong:  " + arg);
				}
				break;
		}
	}

	public String getDescription()
	{
		return "cltest options";
	}

	public static void main(String[] args)
	{
		CommandParser clp = new CommandParser("cltest");
		clp.addCommandListener(new cltest());
		clp.parse(args);

		String[] largs = clp.getUnhandledArguments();
		for(int i = 0; i < largs.length; ++i)
		{
			System.out.println("largs[" + i + "] = '" +
				largs[i] + "'");
		}

		System.out.println("Did you toggle?  " + toggle.getMatched());
	}
}
