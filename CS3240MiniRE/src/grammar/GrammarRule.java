/**
 * 
 */
package grammar;
import parser.MiniREToken;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum GrammarRule {
	PRGM1 ("begin <statement-list> end", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"begin","","end"}),
	PRGM2 ("begin end", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL},  new String[] {"begin","end"}),
	STATEMENTLIST1 ("<statement> <statement-list>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE}, new String[] {"",""}),
	STATEMENTLIST2 ("<statement>", new MiniREToken.Type[] {MiniREToken.Type.RULE}, new String[] {""}),
	STATEMENT1 ("ID := <exp> ;", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"ID",":=","",";"}),
	STATEMENT2 ("replace REGEX with ASCIISTR in <file-names> ;", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"replace","","with","","in","",";"}),
	STATEMENT3 ("print ( <exp-list> ) ;", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL}, new String[] {"print","(","",")",";"}),
	FILENAMES ("<file> -> <file>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}, new String[] {"","->",""}),
	FILE ("ASCIISTR", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}, new String[] {""}),
	EXPLIST1 ("<exp> , <exp-list>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}, new String[] {"",""}),
	EXPLIST2 ("<exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE}, new String[] {""}),
	EXP1 ("ID", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}, new String[] {""}),
	EXP2 ("INTNUM", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}, new String[] {""}),
	EXP3 ("( <exp> )", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"(","",")"}),
	EXP4 ("# <exp>", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}, new String[] {"#",""}),
	EXP5 ("<exp> <bin-op> <exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE, MiniREToken.Type.RULE}, new String[] {"","",""}),
	EXP6 ("find REGEX in <file>", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}, new String[] {"find","","in",""}),
	BINOP1 ("+", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"+"}),
	BINOP2 ("-", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"-"}),
	BINOP3 ("*", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"*"}),
	BINOP4 ("/", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"/"}),
	BINOP5 ("union", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"union"}),
	BINOP6 ("inters", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"inters"}),
	//Bonus part.
	STATEMENT4 ("if ( <condition> ) { <statement-list> } else { <statement-list> }", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"if","(","",")","{","","}","else","{","","}"}),
	STATEMENT5 ("while ( <condition> ) { <statement-list> }", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}, new String[] {"while","(","",")","{","","}"}),
	CONDITION ("<exp> <bool-op> <exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE, MiniREToken.Type.RULE}, new String[] {"","",""}),
	BOOLOP1 ("==", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"=="}),
	BOOLOP2 ("!=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"!="}),
	BOOLOP3 ("<", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"<"}),
	BOOLOP4 (">", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {">"}),
	BOOLOP5 ("<=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {"<="}),
	BOOLOP6 (">=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}, new String[] {">="});
	
	private final String rulestr;
	private final MiniREToken.Type[] ruletypes;
	private final String[] elements;
	GrammarRule(String rulestr, MiniREToken.Type[] ruletypes, String[] elements) {
		this.rulestr = rulestr;
		this.ruletypes = ruletypes;
		this.elements = elements;
	}
	
	public String[] getElements() {
		return elements;
	}

	public String getRulestr() {
		return this.rulestr;
	}
	
	public MiniREToken.Type[] getRuletypes() {
		return ruletypes;
	}
	
	public String toString() {
		return getRulestr();
	}
}
