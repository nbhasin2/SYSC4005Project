package SYSC4005Gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JMenuBar;
import java.awt.Button;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class kFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtK;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JLabel lblIteration;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JLabel lblP;
	private JTextField textField_11;
	private JTextField textField_12;
	private JTextField textField_13;
	private JLabel lblMeanvalue;
	private JLabel lblUpperci;
	private JLabel lblLowerci;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	public kFrame frameThis;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					kFrame frame = new kFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public kFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 697, 418);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSysc = new JLabel("SYSC 4005 - Project Simulation");
		lblSysc.setFont(new Font("Calibri", Font.BOLD, 21));
		lblSysc.setBounds(206, 18, 277, 16);
		contentPane.add(lblSysc);
		frameThis = this;
		Button button = new Button("Topology1 - Round Robin");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			int timeSlots = Integer.parseInt(textField_2.getText());
			int N = Integer.parseInt(textField_5.getText());
			 double probability[] = new double[N];
			 double lambdas[] = new double[N];
			 int iterations= Integer.parseInt(textField_8.getText());
			 double lambda = Double.parseDouble(txtK.getText());
				for (int i = 0; i < N; i++) {
					probability[i] = Double.parseDouble(textField_13.getText());
					lambdas[i] = lambda;
				}
			Topology1 top = new Topology1(timeSlots, probability, lambdas, 1, iterations,frameThis);
			top.runAndPrintToFile("Topology1-RR.txt");
			}
		});
		button.setBounds(10, 98, 222, 29);
		contentPane.add(button);
		
		Button button_1 = new Button("Topology1 - Randomized");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			int timeSlots = Integer.parseInt(textField_3.getText());
			int N = Integer.parseInt(textField_6.getText());
			 double probability[] = new double[N];
			 double lambdas[] = new double[N];
			 int iterations= Integer.parseInt(textField_9.getText());
			 double lambda = Double.parseDouble(textField.getText());
				for (int i = 0; i < N; i++) {
					probability[i] = Double.parseDouble(textField_12.getText());
					lambdas[i] = lambda;
				}
			Topology1 top = new Topology1(timeSlots, probability, lambdas, 3, iterations,frameThis);
			top.runAndPrintToFile("Topology1-RAN.txt");
			}
		});
		button_1.setBounds(10, 147, 222, 29);
		contentPane.add(button_1);
		
		Button button_2 = new Button("Topology1 - LCQ");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			int timeSlots = Integer.parseInt(textField_7.getText());
			int N = Integer.parseInt(textField_7.getText());
			 double probability[] = new double[N];
			 double lambdas[] = new double[N];
			 int iterations= Integer.parseInt(textField_10.getText());
			 double lambda = Double.parseDouble(textField_1.getText());
				for (int i = 0; i < N; i++) {
					probability[i] = Double.parseDouble(textField_11.getText());
					lambdas[i] = lambda;
				}
			Topology1 top = new Topology1(timeSlots, probability, lambdas, 2, iterations,frameThis);
			top.runAndPrintToFile("Topology3-LCQ.txt");
			}
		});
		button_2.setBounds(10, 196, 222, 29);
		contentPane.add(button_2);
		
		JLabel lblTopology = new JLabel("Topology");
		lblTopology.setBounds(96, 74, 61, 16);
		contentPane.add(lblTopology);
		
		txtK = new JTextField();
		txtK.setText("0.2");
		txtK.setBounds(238, 98, 44, 28);
		contentPane.add(txtK);
		txtK.setColumns(10);
		
		JLabel lblLambda = new JLabel("Lambda");
		lblLambda.setBounds(238, 74, 61, 16);
		contentPane.add(lblLambda);
		
		textField = new JTextField();
		textField.setText("0.2");
		textField.setColumns(10);
		textField.setBounds(238, 148, 44, 28);
		contentPane.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setText("0.2");
		textField_1.setColumns(10);
		textField_1.setBounds(238, 197, 44, 28);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setText("50000");
		textField_2.setColumns(10);
		textField_2.setBounds(311, 98, 61, 28);
		contentPane.add(textField_2);
		
		JLabel lblIterations = new JLabel("Time Slots");
		lblIterations.setBounds(311, 74, 73, 16);
		contentPane.add(lblIterations);
		
		textField_3 = new JTextField();
		textField_3.setText("50000");
		textField_3.setColumns(10);
		textField_3.setBounds(311, 148, 61, 28);
		contentPane.add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setText("50000");
		textField_4.setToolTipText("");
		textField_4.setColumns(10);
		textField_4.setBounds(311, 197, 61, 28);
		contentPane.add(textField_4);
		
		JLabel lblQueueLength = new JLabel("Queue Length");
		lblQueueLength.setBounds(396, 74, 87, 16);
		contentPane.add(lblQueueLength);
		
		textField_5 = new JTextField();
		textField_5.setText("5");
		textField_5.setColumns(10);
		textField_5.setBounds(396, 98, 61, 28);
		contentPane.add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setText("5");
		textField_6.setColumns(10);
		textField_6.setBounds(396, 148, 61, 28);
		contentPane.add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setToolTipText("");
		textField_7.setText("5");
		textField_7.setColumns(10);
		textField_7.setBounds(396, 197, 61, 28);
		contentPane.add(textField_7);
		
		lblIteration = new JLabel("Iteration");
		lblIteration.setBounds(495, 74, 61, 16);
		contentPane.add(lblIteration);
		
		textField_8 = new JTextField();
		textField_8.setText("20");
		textField_8.setColumns(10);
		textField_8.setBounds(495, 98, 44, 28);
		contentPane.add(textField_8);
		
		textField_9 = new JTextField();
		textField_9.setText("20");
		textField_9.setColumns(10);
		textField_9.setBounds(495, 148, 44, 28);
		contentPane.add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setText("20");
		textField_10.setColumns(10);
		textField_10.setBounds(495, 197, 44, 28);
		contentPane.add(textField_10);
		
		lblP = new JLabel("p");
		lblP.setBounds(583, 74, 22, 16);
		contentPane.add(lblP);
		
		textField_11 = new JTextField();
		textField_11.setText("1");
		textField_11.setColumns(10);
		textField_11.setBounds(574, 197, 44, 28);
		contentPane.add(textField_11);
		
		textField_12 = new JTextField();
		textField_12.setText("1");
		textField_12.setColumns(10);
		textField_12.setBounds(574, 148, 44, 28);
		contentPane.add(textField_12);
		
		textField_13 = new JTextField();
		textField_13.setText("1");
		textField_13.setColumns(10);
		textField_13.setBounds(574, 98, 44, 28);
		contentPane.add(textField_13);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.GRAY);
		separator.setBounds(6, 250, 685, 6);
		contentPane.add(separator);
		
		JLabel lblSysc_1 = new JLabel("SYSC 4005 - Project Result");
		lblSysc_1.setFont(new Font("Calibri", Font.BOLD, 21));
		lblSysc_1.setBounds(226, 268, 245, 16);
		contentPane.add(lblSysc_1);
		
		lblMeanvalue = new JLabel("MeanValue");
		lblMeanvalue.setBounds(178, 304, 73, 16);
		contentPane.add(lblMeanvalue);
		
		lblUpperci = new JLabel("UpperCI");
		lblUpperci.setBounds(178, 338, 73, 16);
		contentPane.add(lblUpperci);
		
		lblLowerci = new JLabel("LowerCI");
		lblLowerci.setBounds(178, 366, 73, 16);
		contentPane.add(lblLowerci);
		
		textField_14 = new JTextField();
		textField_14.setBounds(282, 296, 257, 28);
		contentPane.add(textField_14);
		textField_14.setColumns(10);
		
		textField_15 = new JTextField();
		textField_15.setColumns(10);
		textField_15.setBounds(282, 332, 257, 28);
		contentPane.add(textField_15);
		
		textField_16 = new JTextField();
		textField_16.setColumns(10);
		textField_16.setBounds(282, 360, 257, 28);
		contentPane.add(textField_16);
	}
	
	//mean
	public JTextField getTF14()
	{
		return textField_14;
	}
	//Upper CI
	public JTextField getTF15()
	{
		return textField_15;
	}
	//Lower CI
	public JTextField getTF16()
	{
		return textField_16;
	}
}