package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

class UniqueNameProvider {
	private Set<String> names = new HashSet<>();
	private int id = 0;
	
	public void addName(String name) {
		names.add(name);
	}
	
	public String getUniqueName() {
		String name = "NamelessState" + id++;
		
		while (names.contains(name)) {
			name = "NamelessState" + id++;
		}
		
		return name;
	}
}

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
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;

		IterableEObject iterable = new IterableEObject(s);
		
		List<State> trapStates = new ArrayList<>();
		List<State> namelessStates = new ArrayList<>();
		UniqueNameProvider provider = new UniqueNameProvider();
		
		for (EObject content : iterable) {
			if(content instanceof State) {
				State state = (State) content;
				
				if (state.getOutgoingTransitions().size() == 0) {
					trapStates.add(state);
				}
				
				if (state.getName().isEmpty()) {
					namelessStates.add(state);
				} else {
					provider.addName(state.getName());
				}
			}
		}
		
		for (EObject content : iterable) {
			if(content instanceof State) {
				State state = (State) content;
				System.out.println(state.getName());
				
				if (state.getName().isEmpty()) {
					namelessStates.add(state);
					
					
					state.setName(provider.getUniqueName());
					System.out.println("Nameless state. Suggesting name: " + state.getName());
				}
			}
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				System.out.println(transition.getSource().getName() + " -> " + transition.getTarget().getName());
			}
		}
		
		System.out.println("\nTrap states:");
		for (State state : trapStates) {
			System.out.println(state.getName());
		}
			
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
