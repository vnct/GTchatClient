
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JTextArea;


public class ChatPriveFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollBar scrollBar = new JScrollBar();
	private JButton btnSend = new JButton("Send");
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JTextArea textClient = new JTextArea();
    private JTextArea textPrive = new JTextArea();
    
	/**
	 * Create the frame.
	 */
	public ChatPriveFrame(LoginFrame loginframe) {
		this.setTitle("Nickname");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		scrollPane.setBounds(5, 5, 393, 176);
		scrollPane.setEnabled(false);
		scrollPane.setViewportView(textPrive);
		contentPane.add(scrollPane);
		
		
		scrollBar.setBounds(404, 5, 17, 176);
		contentPane.add(scrollBar);
		
		btnSend.setBounds(359, 196, 62, 46);
		contentPane.add(btnSend);
		
		scrollPane_1.setBounds(5, 188, 344, 67);
		scrollPane_1.setViewportView(textClient);
		contentPane.add(scrollPane_1);
		
		
		
		contentPane.setLayout(null);
		
		
		
	}

}
