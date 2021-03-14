package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

class IterableEObject implements Iterable<EObject> {
	private EObject eobject;
	
	public IterableEObject(EObject eobject) {
		this.eobject = eobject;
	}

	@Override
	public Iterator<EObject> iterator() {
		return eobject.eAllContents();
	}
}

public class Main {
	
	@Test
	public void test() {
		main(new String[0]);
	}
	
	private static String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;

		IterableEObject iterable = new IterableEObject(s);
		
		List<VariableDefinition> variables = new ArrayList<>();
		List<EventDefinition> events = new ArrayList<>();
		
		for (EObject content : iterable) {
			if(content instanceof VariableDefinition) {
				VariableDefinition variable = (VariableDefinition) content;
				variables.add(variable);
			}
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				events.add(event);
			}
		}
		
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;");
		System.out.println();
		System.out.println("import java.io.BufferedReader;");
		System.out.println("import java.io.IOException;");
		System.out.println("import java.io.InputStreamReader;");
		System.out.println();
		System.out.println("import hu.bme.mit.yakindu.analysis.RuntimeService;");
		System.out.println("import hu.bme.mit.yakindu.analysis.TimerService;");
		System.out.println("import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;");
		System.out.println("import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;");
		System.out.println();
		System.out.println("public class RunStatechart {");
		System.out.println("	public static void main(String[] args) throws IOException {");
		System.out.println("		ExampleStatemachine s = new ExampleStatemachine();");
		System.out.println("		s.setTimer(new TimerService());");
		System.out.println("		RuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println();
		System.out.println("		s.init();");
		System.out.println("		s.enter();");
		System.out.println("		s.runCycle();");
		System.out.println();
		System.out.println("		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));");
		System.out.println();
		System.out.println("		String input = reader.readLine();");
		System.out.println("		boolean running = input != null;");
		System.out.println();
		System.out.println("		while (running) {");
		System.out.println("			switch (input) {");
		for (EventDefinition event : events) {
			String name = event.getName();
			
			System.out.println("			case \"" + name + "\":");
			System.out.println("				s.raise" + capitalize(name) + "();");
			System.out.println("				break;");
		}
		System.out.println("			case \"exit\":");
		System.out.println("				running = false;");
		System.out.println("				continue;");
		System.out.println("			default:");
		System.out.println("				break;");
		System.out.println("			}");
		System.out.println();
		System.out.println("			s.runCycle();");
		System.out.println("			print(s);");
		System.out.println();
		System.out.println("			input = reader.readLine();");
		System.out.println();
		System.out.println("			running = input != null;");
		System.out.println("		}");
		System.out.println("	}");
		System.out.println();
		System.out.println("	public static void print(IExampleStatemachine s) {");
		for (VariableDefinition variable : variables) {
			String id = variable.getId();
			String name = variable.getName();
			
			System.out.println("		System.out.println(\"" + id + " = \" + s.getSCInterface().get" + capitalize(name) + "());\"");
		}
		System.out.println("	}");
		System.out.println("}");

		System.exit(0);
	}
}
