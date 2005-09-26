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
## $Id: parsertest2.rb,v 1.1 2005/09/26 02:27:08 atownley Exp $
##
######################################################################

require 'fqa/fqa'

include FunctionalQA

TEST2_MISSING_ARG = <<EOF
error:  option --arg requires parameter 'ARG'.  Ignored.
EOF

TEST2_MUTEX_ERROR = <<EOF
error:  cannot specify both 'one' and 'two'.  Exiting.
EOF

TEST2_INVALID_COMBO_ERROR = <<EOF
error:  invalid option combination 'A1'.
EOF

TEST2_UNKNOWN_COMBO_SWITCH = <<EOF
error:  unknown switch 'x' specified.
EOF

TEST2_UNKNOWN_COMBO_SWITCH2 = <<EOF
error:  unknown switch 'D' specified.
EOF

TEST2_UNKNOWN_OPTION = <<EOF
error:  unknown option specified (-x).
EOF

TEST2_USAGE = <<EOF
Usage:  test1 [-1|--one] [-2|--two] [-A|--arg ARG]
        [-DKEY=VALUE[,KEY=VALUE...]] [-?|--help] [--usage] FILE...
EOF

class CommandParserTests < TestScript
  def initialize
    super("Command Parser Regression Test Script #2")
  end

  def testcase_missing_arg(prog)
    rc = prog.run(%w( --one --arg ))
    assert_equal(0, rc, "exit on missing arg detected")
    assert_equal(TEST2_MISSING_ARG, prog.errors.join(""))
  end

  def testcase_mutex_short_error(prog)
    rc = prog.run(%w( -12 ))
    assert_equal(2, rc, "1 & 2 are mutex options")
    assert_equal(TEST2_MUTEX_ERROR, prog.errors.join(""))
    assert_equal(TEST2_USAGE, prog.output.join(""))
  end

  def testcase_invalid_combo(prog)
    rc = prog.run(%w( -A1 ))
    assert_equal(0, rc, "invalid combination; they're backwards")
    assert_equal(TEST2_INVALID_COMBO_ERROR, prog.errors.join(""))
    assert_equal(TEST2_USAGE, prog.output.join(""))
  end

  def testcase_unknown_combo_switch(prog)
    rc = prog.run(%w( -x1A ))
    assert_equal(0, rc, "unknown option in a switch")
    assert_equal(TEST2_UNKNOWN_COMBO_SWITCH, prog.errors.join(""))
    assert_equal(TEST2_USAGE, prog.output.join(""))
  end

  def testcase_unknown_option(prog)
    rc = prog.run(%w( -x ))
    assert_equal(0, rc, "unknown option alone")
    assert_equal(TEST2_UNKNOWN_OPTION, prog.errors.join(""))
    assert_equal(TEST2_USAGE, prog.output.join(""))
  end

  def testcase_unknown_joined_switch(prog)
    rc = prog.run(%w( -1D ))
    assert_equal(0, rc, "unknown switch same as joined")
    assert_equal(TEST2_UNKNOWN_COMBO_SWITCH2, prog.errors.join(""))
    assert_equal(TEST2_USAGE, prog.output.join(""))
  end
end

runner = ProgramTestRunner.new(JavaProg.new("test2"))
runner.observer.verbose = false
runner.run(CommandParserTests.new)
