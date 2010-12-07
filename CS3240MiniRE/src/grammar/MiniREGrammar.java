/**
 * 
 */
package grammar;

/**
 * @author Incomprehensible Penguin Arena
 * 
 */

public enum MiniREGrammar {
	PRGM ("<prgm>", new GrammarRule[] {GrammarRule.PRGM1, GrammarRule.PRGM2}),
	STATEMENTLIST ("<statement-list>", new GrammarRule[] {GrammarRule.STATEMENTLIST1, GrammarRule.STATEMENTLIST2}),
	STATEMENT ("<statement>", new GrammarRule[] {GrammarRule.STATEMENT1, GrammarRule.STATEMENT2, GrammarRule.STATEMENT3, GrammarRule.STATEMENT4, GrammarRule.STATEMENT5}),
	FILENAMES ("<file-names>", new GrammarRule[] {GrammarRule.FILENAMES}),
	FILE ("<file>", new GrammarRule[] {GrammarRule.FILE}),
	EXPLIST ("<exp-list>", new GrammarRule[] {GrammarRule.EXPLIST1, GrammarRule.EXPLIST2}),
	EXP ("<exp>", new GrammarRule[] {GrammarRule.EXP1, GrammarRule.EXP2, GrammarRule.EXP3, GrammarRule.EXP4, GrammarRule.EXP5, GrammarRule.EXP6}),
	BINOP ("<bin-op>", new GrammarRule[] {GrammarRule.BINOP1, GrammarRule.BINOP2, GrammarRule.BINOP3, GrammarRule.BINOP4, GrammarRule.BINOP5, GrammarRule.BINOP6}),
	//Bonus part.
	CONDITION ("<condition>", new GrammarRule[] {GrammarRule.CONDITION}),
	BOOLOP ("<bool-op>", new GrammarRule[] {GrammarRule.BOOLOP1, GrammarRule.BOOLOP2, GrammarRule.BOOLOP3, GrammarRule.BOOLOP4, GrammarRule.BOOLOP5, GrammarRule.BOOLOP6});
	
	@SuppressWarnings("unused")
	private final String rulename;
	@SuppressWarnings("unused")
	private final GrammarRule[] rules;
	MiniREGrammar(String name, GrammarRule[] rules) {
		this.rulename = name;
		this.rules = rules;
	}
}
