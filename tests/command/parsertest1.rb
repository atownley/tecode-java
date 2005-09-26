#! /usr/bin/env ruby
######################################################################
##
## Copyright (c) 2005, Andrew S. Townley
## All rights reserved.
## 
## Redistribution and use in source and binary forms, with or without
## modification, are permitted provided that the following conditions
## are met:
## 
##     * Redistributions of source code must retain the above
##     copyright notice, this list of conditions and the following
##     disclaimer.
## 
##     * Redistributions in binary form must reproduce the above
##     copyright notice, this list of conditions and the following
##     disclaimer in the documentation and/or other materials provided
##     with the distribution.
## 
##     * Neither the names Andrew Townley or Townley Enterprises,
##     Inc. nor the names of its contributors may be used to endorse
##     or promote products derived from this software without specific
##     prior written permission.  
## 
## THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
## "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
## LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
## FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
## COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
## INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
## (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
## SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
## HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
## STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
## ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
## OF THE POSSIBILITY OF SUCH DAMAGE.
##
## File:      parsertest1.rb
## Created:   Sun Sep 25 15:49:45 IST 2005
## Author:    Andrew S. Townley <adz1092@yahoo.com>
##
## $Id: parsertest1.rb,v 1.1 2005/09/26 02:27:08 atownley Exp $
##
######################################################################

require 'fqa/fqa'

include FunctionalQA

TEST1_HELP = <<EOF
Usage:  test1 [OPTION...] FILE...

options:
  -1, --one                        option one description
  -2, --two                        option two description
  -A, --arg=ARG                    option arg description

Help options:
  -?, --help                       show this help message
  --usage                          show brief usage message
EOF

TEST1_USAGE = <<EOF
Usage:  test1 [-1|--one] [-2|--two] [-A|--arg ARG] [-?|--help] [--usage]
        FILE...
EOF

class CommandParserTests < TestScript
  def initialize
    super("Command Parser Regression Test Script #1")
  end

  def testcase_mutex_long_error(prog)
    rc = prog.run(%w( --one --two ))
    assert_equal(2, rc, "1 & 2 are mutex options")
  end

  def testcase_requires_arg_long(prog)
    rc = prog.run(%w( --arg foo ))
    assert_equal(3, rc, "arg requires one or two")
  end

  def testcase_requires_arg_short(prog)
    rc = prog.run(%w( -A foo ))
    assert_equal(3, rc, "arg requires one or two")
  end

  def testcase_help_long(prog)
    rc = prog.run(%w( --help ))
    assert_equal(0, rc, "help is a valid option")
    assert_equal(TEST1_HELP, prog.output.join(""), "help output is the same")
  end
  
  def testcase_help_short(prog)
    rc = prog.run(%w( -? ))
    assert_equal(0, rc, "help is a valid option")
    assert_equal(TEST1_HELP, prog.output.join(""), "help output is the same")
  end
  
  def testcase_usage(prog)
    rc = prog.run(%w( --usage ))
    assert_equal(0, rc, "usage is a valid option")
    assert_equal(TEST1_USAGE, prog.output.join(""), "usage output is the same")
  end

  def testcase_missing_arg(prog)
    rc = prog.run(%w( --one --arg ))
    assert_equal(1, rc, "exit on missing arg detected")
  end

end

runner = ProgramTestRunner.new(JavaProg.new("test1"))
runner.observer.verbose = false
runner.run(CommandParserTests.new)
