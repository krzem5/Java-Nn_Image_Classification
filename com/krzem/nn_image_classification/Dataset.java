package com.krzem.nn_image_classification;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;



public class Dataset{
	public static final int IM_SIZE=128;
	private List<int[][]> data;
	private List<String> ll;
	private List<String> all_l;



	public Dataset(){
		this.data=new ArrayList<int[][]>();
		this.ll=new ArrayList<String>();
		this.all_l=new ArrayList<String>();
	}



	public int size(){
		return this.data.size();
	}



	public int[][] getI(int i){
		return this.data.get(i);
	}



	public String getL(int i){
		return this.ll.get(i);
	}



	public List<String> get_all(){
		return this.all_l;
	}



	public void shuffle(){
		List<Integer> idx_l=new ArrayList<Integer>();
		for (int i=0;i<this.data.size();i++){
			idx_l.add(i);
		}
		Collections.shuffle(idx_l);
		List<int[][]> ndata=new ArrayList<int[][]>();
		List<String> nll=new ArrayList<String>();
		for (int idx:idx_l){
			ndata.add(this.data.get(idx));
			nll.add(this.ll.get(idx));
		}
		this.data=ndata;
		this.ll=nll;
	}



	public static Dataset load(String dir,int m){
		Dataset dt=new Dataset();
		for (File f:new File(dir).listFiles()){
			if (!f.isFile()||!f.getName().endsWith(".jpg")){
				continue;
			}
			dt._add(f,m);
		}
		return dt;
	}



	public static BufferedImage get_img(String fp){
		try{
			return (BufferedImage)ImageIO.read(new File(fp));
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}



	public static int[][] convert(BufferedImage i){
		int[][] o=new int[IM_SIZE][IM_SIZE];
		for (int x=0;x<IM_SIZE;x++){
			for (int y=0;y<IM_SIZE;y++){
				Color c=new Color(i.getRGB(x,y));
				o[x][y]=(c.getRed()+c.getGreen()+c.getBlue())/3;
			}
		}
		return o;
	}



	public static int[][] downsample(int[][] i,int m){
		if (i.length%m!=0||i[0].length%m!=0){
			System.out.println("Downsampling failed.");
			return i;
		}
		return Dataset._downsample(i,m);
	}



	private void _add(File f,int m){
		BufferedImage i=Dataset.get_img(f.getAbsolutePath());
		if (i.getWidth()!=IM_SIZE||i.getHeight()!=IM_SIZE){
			System.out.println("Skipping: "+f.getName()+" (Wrong Size)");
			return;
		}
		String l=f.getName().substring(0,f.getName().lastIndexOf("_"));
		this.data.add((m>1?Dataset.downsample(Dataset.convert(i),m):Dataset.convert(i)));
		this.ll.add(l);
		if (this.all_l.indexOf(l)==-1){
			this.all_l.add(l);
		}
	}



	private static int[][] _downsample(int[][] img,int m){
		int[][] o=new int[img.length/m][img[0].length/m];
		for (int i=0;i<img.length/m;i++){
			for (int j=0;j<img.length/m;j++){
				int sum=0;
				for (int k=i*m;k<(i+1)*m;k++){
					for (int l=j*m;l<(j+1)*m;l++){
						sum+=img[k][l];
					}
				}
				o[i][j]=sum/(m*m);
			}
		}
		return o;
	}
}