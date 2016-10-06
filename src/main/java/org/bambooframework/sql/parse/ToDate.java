package org.bambooframework.sql.parse;

public class ToDate extends SQLFunction {
	final static String funcName = "to_date";

	@Override
	public String toSql() {
		StringBuffer sb = new StringBuffer("to_date");
		sb.append("(").append(args.get(0)).append(",").append(args.get(1)).append(")");
		return sb.toString();
	}
}
