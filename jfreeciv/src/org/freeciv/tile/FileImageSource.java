package org.freeciv.tile;
import java.awt.image.*;
import java.io.*;
public class FileImageSource implements ImageProducer
{
  public static int count;
  MemoryImageSource delegate;
  int consumers = 0;
  File file;
  public FileImageSource( File aFile ) 
  {
    count++;
    file = aFile;
  }
  public void addConsumer( ImageConsumer ic )
  {
    consumers++;
    checkDelegate();
    delegate.addConsumer( ic );
  }
  public boolean isConsumer( ImageConsumer ic )
  {
    checkDelegate();
    return delegate.isConsumer( ic );
  }
  public void removeConsumer( ImageConsumer ic )
  {
    if( delegate != null )
    {
      delegate.removeConsumer( ic );
    }
    consumers--;
    if( consumers <= 0 )
    {
      delegate = null;
      consumers = 0;
    }
  }
  public void startProduction( ImageConsumer ic )
  {
    consumers++;
    checkDelegate();
    delegate.startProduction( ic );
  }
  public void requestTopDownLeftRightResend( ImageConsumer ic )
  {
    checkDelegate();
    delegate.requestTopDownLeftRightResend( ic );
  }
  private void checkDelegate()
  {
    if( delegate != null )
    {
      return ;
    }
    if( consumers == 0 )
    {
      throw new RuntimeException( "Delegate called with 0 consumers" );
    }
    try
    {
      ObjectInputStream ois = new ObjectInputStream( new BufferedInputStream( new FileInputStream( file ) ) );
      Data d = (Data)ois.readObject();
      ois.close();
      delegate = new MemoryImageSource( d.w, d.h, cmodel, d.data, 0, d.w );
    }
    catch( IOException e )
    {
      System.out.println( e );
    }
    catch( ClassNotFoundException e )
    {
      System.out.println( e );
    }
  }
  static ColorModel cmodel = new DirectColorModel( 32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000 );
  public static class Data implements Serializable
  {
    public int w;
    public int h;
    public int[] data;
  }
  public void finalize()
  {
    count--;
  }
  public File getFile()
  {
    return file;
  }
}
