/*
 * FileForAndroid.java
 * date 2010/01/28
 * written by shusaku sone
 */

package com.medicaltrust.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public abstract class FileForAndroid {

	public static final String CLASSNAME = "FileForAndroid";
	public static final int  READ                   = 0x0001; //0 0001
	public static final int  WRITE                  = 0x0002; //0 0010
	public static final int  WRITE_APPEND           = 0x0004; //0 0100
	public static final int  WRITE_OTHER_READ       = 0x0008; //0 1000
	public static final int  WRITE_OTHER_WRITE      = 0x0010; //1 0000
	public static final int  WRITE_OTHER_READ_WRITE = 0x0020;
	public static final int  READ_WRITE             = 0x0040;
	public static final int  TEXT_READ    = 0x0080;
	
	/**
	 * InputStream バイト単位で連続的な入力を行う全てのクラスの
	 * スーパクラスです
	 */
	protected InputStream is;

	/**
	 * OutputStream バイト単位で連続的な出力を行う全てのクラスの
	 * スーパクラスで
	 */
	protected OutputStream os;

	/**
	 * InputStreamReader isr
	 * 1文字単位でテキストを読み込みます
	 */
	protected InputStreamReader isr;
	
	/**
	 * OutputStreamReader osw
	 * 1文字単位でテキ�ストを書き込みます
	 */
	protected OutputStreamWriter osw;
	
	/**
	 * BufferReader reader
	 * 1行単位でテキストを読み込みます
	 */
	protected BufferedReader reader;
	
	/**
	 * BufferWriter
	 * 1行単位でテキストを書き込みます
	 */
	protected BufferedWriter writer;
	
	
	/**
	 * 入力ストリームからデータを1バイト読み出します
	 * 読み出したデータは0から255のint型の値として返す
	 */
	public abstract int read();

	/**
	 * 入力ストリームからデータを1行読み出します
	 */
	public abstract byte[] readLine();

	/**
	 * 読み出したデータのバイト数を返す
	 * このメソッドは、read(data, 0, data.length) と記述するのと等価です。
	 */
	public abstract int read(byte[] rdata);

	/**
	 *
	 * 入力ストリー�?��ら指定バイト数�??�??タを指定された byte 配�?の�?��位置へ読み出しま�?
	 *
	 * こ�?メソ�?��は�?��バイト数の�??タの読み出しが完�?��るか�?
	 * 入力ストリー�?��終端に達するか
	 * ある�??例外が throw されるまでブロ�?��します�?
	 *
	 * こ�?メソ�?��で例外が throw される�?は以下�?場合です�?
	 *
	 * 初回の�??タの読み出し中に入力ストリー�?��終端に達した以外�?要因で
	 * 読み出しに失敗した�?合�? IOException �?throw します�?
	 *
	 * index が�?��ある�?? index + length �?data のサイズを�?��る�?合�?
	 * IndexOutOfBoundsException �?throw されます�?
	 *
	 * data �?null 場合�? NullPointerException �?throw されます�?
	 *
	 * これら�?例外が throw された�?合でも�?そこまでに読み込んだ�??タは
	 * data に格納されます�?
	 *
	 * 返り値として実際に読み込んだバイト数を返します�?
	 * 入力ストリー�?��終端に達して�?���??タが読み出せなかった�?合�? -1 を返します�?
	 */
	public abstract int read(byte[] rdata,int offset,int length) throws Exception;

	/**
	 *
	 */
	public abstract void write(final byte[] data);

	/**
	 *
	 */
	public abstract void write(final byte[] data,final int offset,final int size);

	/**
	 * ファイルをオープンします
	 */
	public abstract void open(final String fname,final int mode);

	/**
	 * ファイルをクローズします
	 */
	public abstract void close();

	/**
	 * バッファ中の�??タをフラ�?��ュします
	 */
	public abstract void flush();
}
