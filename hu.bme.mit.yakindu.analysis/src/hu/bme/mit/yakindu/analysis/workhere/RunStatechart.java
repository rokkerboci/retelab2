package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;

public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		
		s.init();
		s.enter();
		s.runCycle();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
		
		String input = reader.readLine();
		
		boolean running = input != null;
		
		while (running) {
			switch (input) {
			case "exit":
				running = false;
				continue;
			case "start":
				s.raiseStart();
				break;
			case "white":
				s.raiseWhite();
				break;
			case "black":
				s.raiseBlack();
				break;
			default:
				break;
			}

			s.runCycle();
			print(s);			
			
			input = reader.readLine();
			
			running = input != null;
		}
		
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
