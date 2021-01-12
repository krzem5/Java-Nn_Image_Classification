package com.krzem.nn_image_classification;



public class Main{
	public static void main(String[] args){
		new Main();
	}



	public Main(){
		Classifier c=new Classifier("./testC","./images/");
		System.out.println(c.test());
		c.train(10,100000,true);
		System.out.println(c.test());
		c.save();
	}
}