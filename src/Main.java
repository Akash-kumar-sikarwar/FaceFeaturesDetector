import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;


@SuppressWarnings("serial")
public class Main extends JPanel implements Runnable, ActionListener{
    JButton snapshot;
    VideoCapture vdcapt;    
	BufferedImage picture;
    MatOfRect faceDetections;
    CascadeClassifier faceDetector;

    Main(){
    	faceDetector = new CascadeClassifier("haarcascade_mcs_eyepair_big.xml");
    	faceDetections = new MatOfRect();
    	snapshot = new JButton("Screenshot");
    	snapshot.addActionListener(this);
    	add(snapshot);
    }
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		 File result = new File("snapshot1.png");
		 int count=0;
		 while(result.exists()){
			 count++;
			 result = new File("snapshot"+count+".png");
		 }
		 try {
			ImageIO.write(picture, "png", result);
		} catch (IOException event1) {
			// TODO Auto-generated catch block
			event1.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 System.loadLibrary("opencv_java2410");
		 vdcapt = new VideoCapture(0);
		 Mat imageofwebcam = new Mat();
		 if(vdcapt.isOpened()){
			 while(true){
				 vdcapt.read(imageofwebcam);
				 if(!imageofwebcam.empty()){
					 JFrame topFrame= (JFrame)SwingUtilities.getWindowAncestor(this);
					 topFrame.setSize(imageofwebcam.width()+40,imageofwebcam.height()+110);
					 matToBufferedImage(imageofwebcam);
					
					 faceDetector.detectMultiScale(imageofwebcam, faceDetections);	
					 System.out.println(String.format("detected %s faces", faceDetections.toArray().length));
					 repaint();
				 }
			 }
		 }

	}
	

	public void matToBufferedImage(Mat matBackground){
		int width = matBackground.width();
		int height = matBackground.height();
		int channels = matBackground.channels();
		byte[]source = new byte[width*height*channels];
		matBackground.get(0,0,source);
		
		picture = new BufferedImage(width, height,BufferedImage.TYPE_3BYTE_BGR);
		final byte[] destination = ((DataBufferByte) picture.getRaster().getDataBuffer()).getData();
		System.arraycopy(source, 0, destination, 0, source.length);
	}
    public void switchCamera(int x){
    	vdcapt = new VideoCapture(x);
    }
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(picture==null) return;
		g.drawImage(picture, 10, 40,picture.getWidth() , picture.getHeight(),null );
        g.setColor(Color.RED);

		for(Rect rect: faceDetections.toArray()){
        	g.drawRect(rect.x+10, rect.y+40, rect.width, rect.height);
        }
	}
}

