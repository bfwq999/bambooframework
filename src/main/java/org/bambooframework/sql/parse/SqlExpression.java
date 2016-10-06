package org.bambooframework.sql.parse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SqlExpression implements Sql {

	String expression;
	
	List args;
	
	public String toSql(){
		if(args == null || args.size()==0){
			return expression;
		}
		String[] newArgs = new String[args.size()];
		for(int i=0; i<args.size(); i++){
			Object obj = args.get(i);
			String str = null;
			if(obj instanceof SQLFunction){
				str = ((SQLFunction)obj).toSql();
			}else{
				str = obj.toString();
			}
			newArgs[i] = str;
		}
		return MessageFormat.format(expression, newArgs);
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public List getArgs() {
		return args;
	}

	public void setArgs(List args) {
		this.args = args;
	}
}
