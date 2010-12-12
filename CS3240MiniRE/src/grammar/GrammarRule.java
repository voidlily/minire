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
	PRGM1 ("begin <statement-list> end", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	PRGM2 ("begin end", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL}),
	STATEMENTLIST1 ("<statement> <statement-list>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE}),
	STATEMENTLIST2 ("<statement>", new MiniREToken.Type[] {MiniREToken.Type.RULE}),
	STATEMENT1 ("ID := <exp> ;", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	STATEMENT2 ("replace REGEX with ASCIISTR in <file-names> ;", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	STATEMENT3 ("print ( <exp-list> ) ;", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL}),
	FILENAMES ("<file> -> <file>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}),
	FILE ("ASCIISTR", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}),
	EXPLIST1 ("<exp> , <exp-list>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}),
	EXPLIST2 ("<exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE}),
	EXP1 ("ID", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}),
	EXP2 ("INTNUM", new MiniREToken.Type[] {MiniREToken.Type.LEXICAL}),
	EXP3 ("( <exp> )", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	EXP4 ("# <exp>", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}),
	EXP5 ("<exp> <bin-op> <exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE, MiniREToken.Type.RULE}),
	EXP6 ("find REGEX in <file>", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.LEXICAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE}),
	BINOP1 ("+", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BINOP2 ("-", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BINOP3 ("*", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BINOP4 ("/", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BINOP5 ("union", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BINOP6 ("inters", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	//Bonus part.
	STATEMENT4 ("if ( <condition> ) { <statement-list> } else { <statement-list> }", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	STATEMENT5 ("while ( <condition> ) { <statement-list> }", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL, MiniREToken.Type.TERMINAL, MiniREToken.Type.RULE, MiniREToken.Type.TERMINAL}),
	CONDITION ("<exp> <bool-op> <exp>", new MiniREToken.Type[] {MiniREToken.Type.RULE, MiniREToken.Type.RULE, MiniREToken.Type.RULE}),
	BOOLOP1 ("==", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BOOLOP2 ("!=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BOOLOP3 ("<", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BOOLOP4 (">", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BOOLOP5 ("<=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL}),
	BOOLOP6 (">=", new MiniREToken.Type[] {MiniREToken.Type.TERMINAL});
	
	private final String rulestr;
	private final MiniREToken.Type[] ruletypes;
	GrammarRule(String rulestr, MiniREToken.Type[] ruletypes) {
		this.rulestr = rulestr;
		this.ruletypes = ruletypes;
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
