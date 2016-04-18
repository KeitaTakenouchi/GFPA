package gfpa.graph.info;


public class Variable
{
	private String variable = new String();


	public Variable(String variable)
	{
		this.variable = variable;
	}

	@Override
	public String toString()
	{
		return variable.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((variable == null) ? 0 : variable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Variable other = (Variable) obj;
		if (variable == null)
		{
			if (other.variable != null) return false;
		}
		else if (!variable.equals(other.variable)) return false;
		return true;
	}

}
