/**
 * AudioArrays
 * Created Oct 28, 2010-3:36:29 PM by Daniel McEnnis
 * Copyright Daniel McEnnis
 *
 * published under the latest APGL. see http://www.fsf.org for license details.
 *
 */
package com.mcennis.archivedj;

import jAudioFeatureExtractor.jAudioTools.AudioMethods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

/**
 * Wrapper class for creating an array of bytes out of a byte file.  This is explicitly using the LGPL 
 * sample code in the DecodeExample of the jorbis code.  The bytes are then transferred into jAudio
 * de-interlaced samples.
 *
 * @author Daniel McEnnis
 *
 */
public class AudioArrays {
	  static int convsize=4096*2;

	  /**
	   * Code a modified copy from DecodeExample of jorbis Ogg Vorbis (LGPL base). Transfers
	   * data from an ogg audio file into a byte array.
	   */
	public static byte[] loadOggAudio(File source) throws FileNotFoundException{
		  byte[] convbuffer=new byte[convsize]; // take 8k out of the data segment, not the stack
		   java.io.InputStream input= new java.io.FileInputStream(source);
		   java.io.ByteArrayOutputStream out = new ByteArrayOutputStream();

		    SyncState oy=new SyncState(); // sync and verify incoming physical bitstream
		    StreamState os=new StreamState(); // take physical pages, weld into a logical stream of packets
		    Page og=new Page(); // one Ogg bitstream page.  Vorbis packets are inside
		    Packet op=new Packet(); // one raw packet of data for decode

		    Info vi=new Info(); // struct that stores all the static vorbis bitstream settings
		    Comment vc=new Comment(); // struct that stores all the bitstream user comments
		    DspState vd=new DspState(); // central working state for the packet->PCM decoder
		    Block vb=new Block(vd); // local working space for packet->PCM decode

		    byte[] buffer;
		    int bytes=0;

		    // Decode setup

		    oy.init(); // Now we can read pages

		    while(true){ // we repeat if the bitstream is chained
		      int eos=0;

		      // grab some data at the head of the stream.  We want the first page
		      // (which is guaranteed to be small and only contain the Vorbis
		      // stream initial header) We need the first page to get the stream
		      // serialno.

		      // submit a 4k block to libvorbis' Ogg layer
		      int index=oy.buffer(4096);
		      buffer=oy.data;
		      try{
		        bytes=input.read(buffer, index, 4096);
		      }
		      catch(Exception e){
		        System.err.println(e);
//		        System.exit(-1);
		      }
		      oy.wrote(bytes);

		      // Get the first page.
		      if(oy.pageout(og)!=1){
		        // have we simply run out of data?  If so, we're done.
		        if(bytes<4096)
		          break;

		        // error case.  Must not be Vorbis data
		        System.err.println("Input does not appear to be an Ogg bitstream.");
//		        System.exit(1);
		      }

		      // Get the serial number and set up the rest of decode.
		      // serialno first; use it to set up a logical stream
		      os.init(og.serialno());

		      // extract the initial header from the first page and verify that the
		      // Ogg bitstream is in fact Vorbis data

		      // I handle the initial header first instead of just having the code
		      // read all three Vorbis headers at once because reading the initial
		      // header is an easy way to identify a Vorbis bitstream and it's
		      // useful to see that functionality seperated out.

		      vi.init();
		      vc.init();
		      if(os.pagein(og)<0){
		        // error; stream version mismatch perhaps
		        System.err.println("Error reading first page of Ogg bitstream data.");
//		        System.exit(1);
		      }

		      if(os.packetout(op)!=1){
		        // no page? must not be vorbis
		        System.err.println("Error reading initial header packet.");
//		        System.exit(1);
		      }

		      if(vi.synthesis_headerin(vc, op)<0){
		        // error case; not a vorbis header
		        System.err
		            .println("This Ogg bitstream does not contain Vorbis audio data.");
//		        System.exit(1);
		      }

		      // At this point, we're sure we're Vorbis.  We've set up the logical
		      // (Ogg) bitstream decoder.  Get the comment and codebook headers and
		      // set up the Vorbis decoder

		      // The next two packets in order are the comment and codebook headers.
		      // They're likely large and may span multiple pages.  Thus we reead
		      // and submit data until we get our two pacakets, watching that no
		      // pages are missing.  If a page is missing, error out; losing a
		      // header page is the only place where missing data is fatal. */

		      int i=0;
		      while(i<2){
		        while(i<2){

		          int result=oy.pageout(og);
		          if(result==0)
		            break; // Need more data
		          // Don't complain about missing or corrupt data yet.  We'll
		          // catch it at the packet output phase

		          if(result==1){
		            os.pagein(og); // we can ignore any errors here
		            // as they'll also become apparent
		            // at packetout
		            while(i<2){
		              result=os.packetout(op);
		              if(result==0)
		                break;
		              if(result==-1){
		                // Uh oh; data at some point was corrupted or missing!
		                // We can't tolerate that in a header.  Die.
		                System.err.println("Corrupt secondary header.  Exiting.");
//		                System.exit(1);
		              }
		              vi.synthesis_headerin(vc, op);
		              i++;
		            }
		          }
		        }
		        // no harm in not checking before adding more
		        index=oy.buffer(4096);
		        buffer=oy.data;
		        try{
		          bytes=input.read(buffer, index, 4096);
		        }
		        catch(Exception e){
		          System.err.println(e);
//		          System.exit(1);
		        }
		        if(bytes==0&&i<2){
		          System.err.println("End of file before finding all Vorbis headers!");
//		          System.exit(1);
		        }
		        oy.wrote(bytes);
		      }

		      // Throw the comments plus a few lines about the bitstream we're
		      // decoding
		      {
		        byte[][] ptr=vc.user_comments;
		        for(int j=0; j<ptr.length; j++){
		          if(ptr[j]==null)
		            break;
		          System.err.println(new String(ptr[j], 0, ptr[j].length-1));
		        }
		        System.err.println("\nBitstream is "+vi.channels+" channel, "+vi.rate
		            +"Hz");
		        System.err.println("Encoded by: "
		            +new String(vc.vendor, 0, vc.vendor.length-1)+"\n");
		      }

		      convsize=4096/vi.channels;

		      // OK, got and parsed all three headers. Initialize the Vorbis
		      //  packet->PCM decoder.
		      vd.synthesis_init(vi); // central decode state
		      vb.init(vd); // local state for most of the decode
		      // so multiple block decodes can
		      // proceed in parallel.  We could init
		      // multiple vorbis_block structures
		      // for vd here

		      float[][][] _pcm=new float[1][][];
		      int[] _index=new int[vi.channels];
		      // The rest is just a straight decode loop until end of stream
		      while(eos==0){
		        while(eos==0){

		          int result=oy.pageout(og);
		          if(result==0)
		            break; // need more data
		          if(result==-1){ // missing or corrupt data at this page position
		            System.err
		                .println("Corrupt or missing data in bitstream; continuing...");
		          }
		          else{
		            os.pagein(og); // can safely ignore errors at
		            // this point
		            while(true){
		              result=os.packetout(op);

		              if(result==0)
		                break; // need more data
		              if(result==-1){ // missing or corrupt data at this page position
		                // no reason to complain; already complained above
		              }
		              else{
		                // we have a packet.  Decode it
		                int samples;
		                if(vb.synthesis(op)==0){ // test for success!
		                  vd.synthesis_blockin(vb);
		                }

		                // **pcm is a multichannel float vector.  In stereo, for
		                // example, pcm[0] is left, and pcm[1] is right.  samples is
		                // the size of each channel.  Convert the float values
		                // (-1.<=range<=1.) to whatever PCM format and write it out

		                while((samples=vd.synthesis_pcmout(_pcm, _index))>0){
		                  float[][] pcm=_pcm[0];
		                  int bout=(samples<convsize ? samples : convsize);

		                  // convert floats to 16 bit signed ints (host order) and
		                  // interleave
		                  for(i=0; i<vi.channels; i++){
		                    int ptr=i*2;
		                    //int ptr=i;
		                    int mono=_index[i];
		                    for(int j=0; j<bout; j++){
		                      int val=(int)(pcm[i][mono+j]*32767.);
		                      //		      short val=(short)(pcm[i][mono+j]*32767.);
		                      //		      int val=(int)Math.round(pcm[i][mono+j]*32767.);
		                      // might as well guard against clipping
		                      if(val>32767){
		                        val=32767;
		                      }
		                      if(val<-32768){
		                        val=-32768;
		                      }
		                      if(val<0)
		                        val=val|0x8000;
		                      convbuffer[ptr]=(byte)(val);
		                      convbuffer[ptr+1]=(byte)(val>>>8);
		                      ptr+=2*(vi.channels);
		                    }
		                  }

		                  out.write(convbuffer, 0, 2*vi.channels*bout);

		                  // tell libvorbis how
		                  // many samples we
		                  // actually consumed
		                  vd.synthesis_read(bout);
		                }
		              }
		            }
		            if(og.eos()!=0)
		              eos=1;
		          }
		        }
		        if(eos==0){
		          index=oy.buffer(4096);
		          buffer=oy.data;
		          try{
		            bytes=input.read(buffer, index, 4096);
		          }
		          catch(Exception e){
		            System.err.println(e);
//		            System.exit(1);
		          }
		          oy.wrote(bytes);
		          if(bytes==0)
		            eos=1;
		        }
		      }

		      // clean up this logical bitstream; before exit we see if we're
		      // followed by another [chained]

		      os.clear();

		      // ogg_page and ogg_packet structs always point to storage in
		      // libvorbis.  They're never freed or manipulated directly

		      vb.clear();
		      vd.clear();
		      vi.clear(); // must be called last
		    }

		    // OK, clean up the framer
		    oy.clear();
		    System.err.println("Done.");
		return out.toByteArray();
	}
	
	/**
	 * Wraps loadOggAudio to create de-interlaced jAudio sample arrays.
	 * 
	 * @param source file containing the ogg audio file.
	 * @return de-interlaced jAudio sample arrays.
	 * @throws Exception if there is a problem with the files existence or its formatting.
	 */
	public static double[][] extract_sample_values(File source) throws Exception{
		byte[] audio_bytes = loadOggAudio(source);

		// Note the AudioFormat
		AudioFormat this_audio_format = new AudioFormat((float) 44100.0, 16, 2, true, true);

		// Extract information from this_audio_format
		int number_of_channels = this_audio_format.getChannels();
		int bit_depth = this_audio_format.getSampleSizeInBits();

		// Throw exception if incompatible this_audio_format provided
		if ( (bit_depth != 16 && bit_depth != 8 )||
		     !this_audio_format.isBigEndian() ||
		     this_audio_format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED )
			throw new Exception( "Only 8 or 16 bit signed PCM samples with a big-endian\n" +
			                     "byte order can be analyzed currently." );

		// Find the number of samples in the audio_bytes
		int number_of_bytes = audio_bytes.length;
		int bytes_per_sample = bit_depth / 8;
		int number_samples = number_of_bytes / bytes_per_sample / number_of_channels;

		// Throw exception if incorrect number of bytes given
		if ( ((number_samples == 2 || bytes_per_sample == 2) && (number_of_bytes % 2 != 0)) ||
		     ((number_samples == 2 && bytes_per_sample == 2) && (number_of_bytes % 4 != 0)) )
			throw new Exception("Uneven number of bytes for given bit depth and number of channels.");
		
		// Find the maximum possible value that a sample may have with the given
		// bit depth
		double max_sample_value = AudioMethods.findMaximumSampleValue(bit_depth) + 2.0;

		// Instantiate the sample value holder
		double[][] sample_values = new double[number_of_channels][number_samples];

		// Convert the bytes to double samples
		ByteBuffer byte_buffer = ByteBuffer.wrap(audio_bytes);
		if (bit_depth == 8)
		{
			for (int samp = 0; samp < number_samples; samp++)
				for (int chan = 0; chan < number_of_channels; chan++)
					sample_values[chan][samp] = (double) byte_buffer.get() / max_sample_value;
		}
		else if (bit_depth == 16)
		{
			ShortBuffer short_buffer = byte_buffer.asShortBuffer();
			for (int samp = 0; samp < number_samples; samp++)
				for (int chan = 0; chan < number_of_channels; chan++)
					sample_values[chan][samp] = (double) short_buffer.get() / max_sample_value;
		}

		// Return the samples
		return sample_values;

	}
	
}
