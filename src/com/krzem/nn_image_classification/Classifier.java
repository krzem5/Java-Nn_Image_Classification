package com.krzem.nn_image_classification;



import com.krzem.NN.NeuralNetwork;
import java.io.File;



public class Classifier{
	public static final int IM_SIZE=32;
	private String fp;
	private String dtp;
	public Dataset dt;
	public Dataset tdt;
	public NeuralNetwork nn;



	public Classifier(String p,String dtp){
		this.fp=p;
		this.dtp=dtp;
		this.dt=Dataset.load(this.dtp+"train/",Dataset.IM_SIZE/IM_SIZE);
		this.tdt=Dataset.load(this.dtp+"test/",Dataset.IM_SIZE/IM_SIZE);
		if (new File(this.fp+".nn-data").exists()==true){
			this.nn=NeuralNetwork.fromFile(this.fp);
		}
		else{
			this.nn=new NeuralNetwork(IM_SIZE*IM_SIZE,new int[]{1024+128},dt.get_all().size(),7.5e-3);
		}
	}



	public String classify(int[][] img){
		return this._vec_to_label(this.nn.predict(this._img_to_vec(img)));
	}



	public void train(int minE,int maxE,boolean log){
		double ca=0;
		double pa=0;
		if (log==true){
			System.out.println(String.format("Start accuracy: %f",this.test()));
		}
		for (int e=0;e<maxE;e++){
			int l=-1;
			for (int i=0;i<this.dt.size();i++){
				if (log==true&&Math.floor((double)(i)/this.dt.size()*100)>l){
					l=(int)Math.floor((double)(i)/this.dt.size()*100);
					System.out.println(String.format("%d%% complete...",l));
				}
				this.nn.train(this._img_to_vec(this.dt.getI(i)),this._label_to_vec(this.dt.getL(i)));
			}
			ca=this.test();
			if (log==true){
				int w=40+String.format("%d%d%f",e+1,maxE,ca).length();
				String b="";
				for (int i=0;i<w;i++){
					b+="=";
				}
				System.out.println(String.format("%s\nEpoch %d/%d complete (acc=%f)\n%s",b,e+1,maxE,ca,b));
			}
			this.save(this.fp+"-"+Integer.toString(e));
			if (ca<pa&&e>=minE+1){
				break;
			}
			pa=ca+0;
		}
	}



	public double test(){
		int acc=0;
		for (int i=0;i<this.tdt.size();i++){
			if (this._vec_to_label(this.nn.predict(this._img_to_vec(this.tdt.getI(i)))).equals(this.tdt.getL(i))){
				acc++;
			}
		}
		return (double)(acc)/this.tdt.size()*100;
	}



	public void save(){
		this.nn.toFile(this.fp);
	}



	public void save(String fp){
		this.nn.toFile(fp);
	}



	private String _vec_to_label(double[] v){
		double m=Double.MIN_VALUE;
		int mi=-1;
		for (int i=0;i<v.length;i++){
			if (v[i]>m){
				m=v[i]+0;
				mi=i+0;
			}
		}
		return this.dt.get_all().get(mi);
	}



	private double[] _label_to_vec(String l){
		double[] v=new double[this.dt.get_all().size()];
		v[this.dt.get_all().indexOf(l)]=1;
		return v;
	}



	private double[] _img_to_vec(int[][] img){
		double[] o=new double[img.length*img[0].length];
		for (int i=0;i<img.length;i++){
			for (int j=0;j<img[0].length;j++){
				o[i*img.length+j]=img[i][j];
			}
		}
		return o;
	}
}