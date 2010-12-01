/**
 * 
 */
package grammar;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum GrammarRule {
	//TODO: Add the terminal/lexical/rule representation for each rule for easy parsing.
	PRGM1 ("begin <statement-list> end"),
	PRGM2 ("begin end"),
	STATEMENTLIST1 ("<statement> <statement-list>"),
	STATEMENTLIST2 ("<statement>"),
	STATEMENT1 ("ID := <exp> ;"),
	STATEMENT2 ("replace REGEX with ASCIISTR in <file-names> ;"),
	STATEMENT3 ("print ( <exp-list> ) ;"),
	FILENAMES ("<file> -> <file>"),
	FILE ("ASCIISTR"),
	EXPLIST1 ("<exp> , <exp-list>"),
	EXPLIST2 ("<exp>"),
	EXP1 ("ID"),
	EXP2 ("INTNUM"),
	EXP3 ("( <exp> )"),
	EXP4 ("# <exp>"),
	EXP5 ("<exp> <bin-op> <exp>"),
	EXP6 ("find REGEX in <file>"),
	BINOP1 ("+"),
	BINOP2 ("-"),
	BINOP3 ("*"),
	BINOP4 ("/"),
	BINOP5 ("union"),
	BINOP6 ("inters"),
	//Bonus part.
	STATEMENT4 ("if ( <condition> ) { <statement-list> } else { <statement-list> }"),
	STATEMENT5 ("while ( <condition> ) { <statement-list> }"),
	CONDITION ("<exp> <bool-op> <exp>"),
	BOOLOP1 ("=="),
	BOOLOP2 ("!="),
	BOOLOP3 ("<"),
	BOOLOP4 (">"),
	BOOLOP5 ("<="),
	BOOLOP6 (">=");
	
	private final String rulestr;
	GrammarRule(String rulestr) {
		this.rulestr = rulestr;
	}
}
