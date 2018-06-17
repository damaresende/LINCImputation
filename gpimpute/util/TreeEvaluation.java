/**
* The class <code>TreeEvaluation</code> reads a tree structure and
* finds the equation that it represents.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package util;

import java.util.Stack;

public class TreeEvaluation {
    
    /**
     * Reads each node of a tree using prefix evaluation, finds the
     * equation that it represents and calculates the value of the sample.
     * 
     * @param tree string represent the tree
     * @param x values of the samples of each attribute at a given index
     * @return value of the sample estimated
     */
    public static double prefixEvaluation (String tree, double x[]) {
        String expression = "";
        for(int i = 1; i < tree.length(); i++)
            if(tree.charAt(i) == '(')
            	expression += tree.charAt(i) + " ";
            else if(tree.charAt(i) == ')')
            	expression += " " + tree.charAt(i);
            else
            	expression += tree.charAt(i);
                
        String token;
        String tokens[] = expression.split(" ");
        
        Stack<Double> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();
        
        for(int i = tokens.length - 1; i >= 0; i--) {
            token = tokens[i];
		    if(tokens[i].compareTo("") != 0) {
				if(Character.isDigit(token.charAt(0)))
				    operandStack.push(Double.parseDouble(token));
				else if(token.charAt(0) == 'X') {
				    if(Character.isDigit(token.charAt(3)))
				    	operandStack.push(x[Integer.parseInt(token.charAt(2) + "" + token.charAt(3))]);
				    else {
				    	operandStack.push(x[Integer.parseInt(token.charAt(2) + "")]);
				    }
				} else if(token.compareTo("(") == 0) {
				    operandStack.push(operate(operandStack, operatorStack.pop()));
				} else if(token.compareTo(")") != 0)
					operatorStack.push(token);
		    }
        }
        return operandStack.peek();
    }

    /**
     * Operands that can be considered in the tree.
     * 
     * @param operandStack stack that has each operand in the tree.
     * @param operator value to use in computation
     * @return value calculated
     */
    public static double operate(Stack<Double> operandStack, String operator) {
        switch(operator) {
            case "*":
                return operandStack.pop() * operandStack.pop();
            case "//":
                return operandStack.pop() / operandStack.pop();
            case "+":
                return operandStack.pop() + operandStack.pop();
            case "-":
                return operandStack.pop() - operandStack.pop();
	    case "sqrt":
                return Math.sqrt(operandStack.pop());
            case "sin":
                return Math.sin(operandStack.pop());
            case "cos":
                return Math.cos(operandStack.pop());
            case "abs":
                return Math.abs(operandStack.pop());
            case "exp":
                return Math.abs(operandStack.pop());    
            default:
                throw new IllegalStateException("Unknown operator " + operator + " ");
       }
    }
}
