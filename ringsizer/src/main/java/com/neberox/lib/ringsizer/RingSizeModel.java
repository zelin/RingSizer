package com.neberox.lib.ringsizer;

public class RingSizeModel
{
	public float diameter;
	public String usa;
	public String japan;
	public String australia;
	public String europe;

	public RingSizeModel(float diameter, String usa, String australia, String europe, String japan)
	{
		this.australia 	= australia;
		this.europe 	= europe;
		this.japan 		= japan;
		this.diameter 	= diameter;
		this.usa 		= usa;
	}	
}
