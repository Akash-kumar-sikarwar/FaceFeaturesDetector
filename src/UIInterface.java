import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.opencv.highgui.VideoCapture;

@SuppressWarnings("serial")
public class UIInterface extends JFrame implements ActionListener {
	Main camerapanel;
	UIInterface(){
		 System.loadLibrary("opencv_java2410");
			JMenuBar bar= new JMenuBar();
			JMenu camera = new JMenu("Camera Window");
			camerapanel=new Main();
			VideoCapture vocab=new VideoCapture(0);
			Thread thraed = new Thread(camerapanel);
			bar.add(camera);
			int count=1;
			while(vocab.isOpened()){
				JMenuItem cam = new JMenuItem("Camera "+1);
				cam.addActionListener(this);
				camera.add(cam);
				vocab.release();
				vocab=new VideoCapture(count);
				count++;
			}
			thraed.start();
			add(camerapanel);
			setJMenuBar(bar);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setSize(500,500);
			setVisible(true);
	}
	 
	@Override
	public void actionPerformed(ActionEvent a) {
		// TODO Auto-generated method stub
		JMenuItem source = (JMenuItem) a.getSource();
		int cameranumber = Integer.parseInt(source.getText().substring(7))-1;
		camerapanel.switchCamera(cameranumber);
		
	}
	public static void main(String args[]){
		 UIInterface cameraframe= new UIInterface();
	 }
}
