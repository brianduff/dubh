package org.freeciv.net;

import java.io.InputStream;
import java.io.IOException;

import org.freeciv.common.Logger;

public class InStream {

	InputStream input;
	byte[] inData = new byte[16000];
	int inPtr;
	short inputPacketLength;
	int inputPacketType; /*byte in fact*/

	public InStream(InputStream anInput) {
		input = anInput;
	}

	public boolean hasMoreData()
	{
		return inPtr < inputPacketLength;
	}
	
	public int packIterRemaining()
	{
	  return inputPacketLength - inPtr;
	}

	public void recvPacket() throws IOException
	{
		if ( inPtr != inputPacketLength )
			throw new RuntimeException("Packet length of " +inputPacketLength +
                           			" type " + inputPacketType +
                           			" but read " + inPtr);

		inPtr = 0;
		int d0 = input.read();
		int d1 = input.read();
		inData[0] = (byte)d0;
		inData[1] = (byte)d1;
		inputPacketLength =(short) ( (d0<<8) + d1);
		int alreadyRead = 2;
		while ( alreadyRead < inputPacketLength )
		{
			alreadyRead += input.read(inData, alreadyRead, inputPacketLength-alreadyRead);
		}
		inputPacketType = inData[2];
		inPtr = 3;
	}

	public int getInputPacketType()
	{
		return inputPacketType;
	}



	public String readZeroString()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		StringBuffer sb = new StringBuffer(100);
		byte b = inData[inPtr++];
		while ( b != 0 )
		{
			sb.append((char)b);
			b = inData[inPtr++];
		}
		if (Logger.DEBUG)
		{
		   Logger.log(Logger.LOG_DEBUG,
		      "readString: "+sb.toString()
		   );
		}
		return sb.toString();
	}

	public byte readByte()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		byte b = inData[inPtr++];
		if (Logger.DEBUG)
		{
		   Logger.log(Logger.LOG_DEBUG,
		      "ReadByte: "+b
		   );
		}
		return b;
	}

	public int readUnsignedByte()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		int i = ((int)inData[inPtr++])&0xff;
		if (Logger.DEBUG)
		{
		   Logger.log(Logger.LOG_DEBUG, "ReadUnsignedByte: "+i);
		}
		return i;
	}


	public int readShort()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		int s = ((inData[inPtr++]<<8)&0xff00) | (inData[inPtr++]&0xff);
	   if (Logger.DEBUG)
	   {
	      Logger.log(Logger.LOG_DEBUG, "ReadShort: "+s);
	   }
	  return s;
	}

	public int readInt()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		int i=
			((inData[inPtr++]<<24)&0xff000000) |
			((inData[inPtr++]<<16)&0xff0000) |
			((inData[inPtr++]<<8)&0xff00) |
			(inData[inPtr++]&0xff);
	
		if (Logger.DEBUG)
		{
		   Logger.log(Logger.LOG_DEBUG, "ReadInt: "+i);
		}
		return i;
	}

	public boolean[] readBitString()
	{
		if ( inPtr >= inputPacketLength ) throw new RuntimeException();

		int length = readUnsignedByte();
		boolean[] arr = new boolean[length];
		for ( int i =0; i < length; )
		{
			int data= readByte();
			for(int b=0;b<8 && i<length;b++,i++)
			{
				if((data&(1<<b)) != 0)
					arr[i]=true;
			}
		}
		return arr;
	}
	
	
	// This has to go here, because it's low level stuff
	public char[] readCityMap()
	{
	  char[] str = new char[26];  // ?? const
	  final int index[] =
      {1,2,3, 5,6,7,8,9, 10,11, 13,14, 15,16,17,18,19, 21,22,23 };	

    str[0] = '2'; str[4] = '2'; str[12]='1';
    str[20] = '2'; str[24] = '2'; str[25] = '\0';

    for (int i=0; i <20; )
    {
      if (false) // piter->short_packet > 0
      {
        for (int j=0; j < 5; j++)
        {
          str[index[i++]] = '0';
        }
      }
      else
      {
        byte b = inData[inPtr++];
        str[index[i++]]=(char)('0'+(char)b/81); b%=81;
        str[index[i++]]=(char)('0'+(char)b/27); b%=27;
        str[index[i++]]=(char)('0'+(char)b/9); b%=9;
        str[index[i++]]=(char)('0'+(char)b/3); b%=3;
        str[index[i++]]=(char)('0'+(char)b);
      }
    }

    return str;

	}

	public String dumpData()
	{
		StringBuffer sb = new StringBuffer(4000);
		sb.append(" type=").append(inputPacketType);
		sb.append(" length=").append(inputPacketLength);
		sb.append(" inPtr=").append(inPtr);
		sb.append(" data=");
		int x = inPtr;
		while (x < inputPacketLength )
			sb.append(inData[x++]).append(',');
		return sb.toString();
	}


	public void consume()
	{
		inPtr = inputPacketLength;
	}

	public void rewind()
	{
		inPtr = 3;
	}

}
