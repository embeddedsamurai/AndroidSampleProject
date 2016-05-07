/*
 * LocalFileForAndroid.java
 * date 2010/01/28
 * written by embedded.samurai
 */

package com.medicaltrust.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class LocalFileForAndroid extends FileForAndroid{

	public static final String CLASSNAME = "FileForAndroid";
	Context context;
	
	
	public LocalFileForAndroid(Context context){
		this.context = context;
	}


	//--実行時例外 サイズが超えている ---
	public class ArraySizeOutOfBoundsError extends Exception{
		public ArraySizeOutOfBoundsError(String str){
			Log.e(CLASSNAME,"str="+str);
		}
	}

	/** ファイルをオープンする */
	public  void open(final String fname,final int mode){
		
		// ファイルをオープンする
		try{
			String name = null;

			if(mode == FileForAndroid.READ){
			
				is =context.openFileInput(fname);
			
			//書きこんだファイルはそのアプリからしか読むことはできない
			}else if(mode == FileForAndroid.WRITE){
				
				os =context.openFileOutput(fname,Context.MODE_PRIVATE);
			
			//追加の書き込みをするときはこのモード
			}else if(mode == FileForAndroid.WRITE_APPEND ){
				
				os =context.openFileOutput(fname,Context.MODE_APPEND);
			
			//書きこんだファイルが他のアプリからも読み込み可能にする
			}else if(mode == FileForAndroid.WRITE_OTHER_READ){
			
				os =context.openFileOutput(fname, Context.MODE_WORLD_READABLE);
			
			//書きこんだファイルが他のアプリからも書き込み可能にする
			}else if(mode == FileForAndroid.WRITE_OTHER_WRITE){
				
				os =context.openFileOutput(fname, Context.MODE_WORLD_WRITEABLE);
			
			//書きこんだファイルが他のアプリからも読み書き可能にする
			}else if(mode == FileForAndroid.WRITE_OTHER_READ_WRITE){
				
				os =context.openFileOutput(fname, Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
			
			}else if(mode == FileForAndroid.TEXT_READ){
				
				is =context.openFileInput(fname);
				//一文字読み込み InputStreamReader
				//作成したテキストの文字コードを第2引数に指定する
				//テキストはなるべくUTF-8で作成すること
				//文字コードが別のテキストを用意したときは第2引数をその文字コードに合わせること
				isr = new InputStreamReader(is,"UTF-8");
				reader=new BufferedReader(isr);
				
			}
		
		} catch(Exception e){
			this.close();
			e.printStackTrace();
			Log.e(CLASSNAME,"File Error");
		}
	}

	/**
	 * ファイルストリームをクローズする
	 */
	public  void close(){
		try{
			if( reader != null) reader.close();
			if( writer != null) writer.close();
			if( isr != null) isr.close();
			if( osw != null) osw.close();
			if( is != null ) is.close();
			if( os != null ) os.close();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	
	/**
	 * rdata配列のoffsetの位置からlength分、データを読み込む
	 *
	 * @return 読み込んだデータの数
	 */
	public  int read(byte[] rdata,int offset,int length) throws ArraySizeOutOfBoundsError{

		if(length > rdata.length){
			throw new ArraySizeOutOfBoundsError("size too large");
		}
		int size=0;
		try{
			size=is.read(rdata, offset,length);
		}catch(Exception e){
			Log.e(CLASSNAME,"can't read");
		}
	return size;
}

	/**
	 * rdata配列の0の位置からrdata.length分、データを読み込む
	 *
	 * @return 読み込んだデータの数
	 */
	public  int read(byte[] rdata){

		int size=0;
		try{
			size=is.read(rdata,0,rdata.length);
		}catch(Exception e){
			Log.e(CLASSNAME,"can't read");
		}

		return size;
	}


	/**
	 * ファイルから1バイト読み出す
	 *
	 * @return 読み込んだ1バイトデータ
	 */
	public  int read(){
		int data=0;
		try{
			data = is.read();
		}catch(Exception e){
			Log.e(CLASSNAME,"can't read");
		}
		return data;
	}

	/**
	 * ファイルから1行テキストで読みだす
	 * @return
	 */
	public String text_read(){
		
		String str="";
		try{
			str=reader.readLine();
		}catch(Exception e){
			Log.e(CLASSNAME,"Can't Text Read");
		}
		return str;
	}
	

	/**
	 * ファイルから1行読み出す
	 *
	 * @return 読み込んだ1行のデータ
	 */
	public  byte[] readLine(){
		ByteArrayOutputStream buf = new ByteArrayOutputStream();

		while(true){
			int data = 0;
			try{
				data = is.read();
			} catch(Exception e){
				e.printStackTrace();
			}
			// ファイルの末端または改行を見つけたとき
			if( data < 0 || data == '\n'){
				return buf.toByteArray();
			}else{
				buf.write(data);
			}
		}//end of while
	}


	/**
	 * ファイルにデータを書き込む
	 *
	 * @param data   書き込むデータ
	 * @param offset
	 * @param size
	 */
	public  void write(final byte[] data,final int offset,final int size){
		try{
			os.write(data,offset,size);
		}catch(Throwable e){
			Log.e(CLASSNAME,"can't write stack data");
		}
	}

	/**
	 * ファイルにデータを書き込む
	 *
	 * @param data 書き込むデータ
	 *
	 */
	public  void write(final byte[] data){
		try{
			os.write(data);
		}catch(Throwable e){
			Log.e(CLASSNAME,"can't write stack data");
		}
	}

	/**
	 * バッファ中のデータをフラッシュする
	 *
	 */
	public  void flush(){
		try{
			os.flush();
		}catch (Exception e) {
			Log.e(CLASSNAME,"can't flush data");
		}
	}
}
