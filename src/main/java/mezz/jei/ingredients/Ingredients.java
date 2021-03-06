package mezz.jei.ingredients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import mezz.jei.Internal;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IIngredients;

public class Ingredients implements IIngredients {
	private final Map<Class, List<List>> inputs = new IdentityHashMap<Class, List<List>>();
	private final Map<Class, List<List>> outputs = new IdentityHashMap<Class, List<List>>();

	@Override
	public <T> void setInput(Class<? extends T> ingredientClass, T input) {
		setInputs(ingredientClass, Collections.singletonList(input));
	}

	@Override
	public <T> void setInputLists(Class<? extends T> ingredientClass, List<List<T>> inputs) {
		IIngredientRegistry ingredientRegistry = Internal.getIngredientRegistry();
		IIngredientHelper<T> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		List<List> expandedInputs = new ArrayList<List>();
		for (List<T> input : inputs) {
			List<T> itemStacks = ingredientHelper.expandSubtypes(input);
			expandedInputs.add(itemStacks);
		}

		this.inputs.put(ingredientClass, expandedInputs);
	}

	@Override
	public <T> void setInputs(Class<? extends T> ingredientClass, List<T> input) {
		IIngredientRegistry ingredientRegistry = Internal.getIngredientRegistry();
		IIngredientHelper<T> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		List<List> expandedInputs = new ArrayList<List>();
		for (T input1 : input) {
			List<T> itemStacks = ingredientHelper.expandSubtypes(Collections.singletonList(input1));
			expandedInputs.add(itemStacks);
		}

		this.inputs.put(ingredientClass, expandedInputs);
	}

	@Override
	public <T> void setOutput(Class<? extends T> ingredientClass, T output) {
		setOutputs(ingredientClass, Collections.singletonList(output));
	}

	@Override
	public <T> void setOutputs(Class<? extends T> ingredientClass, List<T> outputs) {
		IIngredientRegistry ingredientRegistry = Internal.getIngredientRegistry();
		IIngredientHelper<T> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		List<List> expandedOutputs = new ArrayList<List>();
		for (T output : outputs) {
			List<T> expandedOutput = ingredientHelper.expandSubtypes(Collections.singletonList(output));
			expandedOutputs.add(expandedOutput);
		}

		this.outputs.put(ingredientClass, expandedOutputs);
	}

	@Override
	public <T> void setOutputLists(Class<? extends T> ingredientClass, List<List<T>> outputs) {
		IIngredientRegistry ingredientRegistry = Internal.getIngredientRegistry();
		IIngredientHelper<T> ingredientHelper = ingredientRegistry.getIngredientHelper(ingredientClass);
		List<List> expandedOutputs = new ArrayList<List>();
		for (List<T> output : outputs) {
			List<T> itemStacks = ingredientHelper.expandSubtypes(output);
			expandedOutputs.add(itemStacks);
		}

		this.outputs.put(ingredientClass, expandedOutputs);
	}

	@Override
	public <T> List<List<T>> getInputs(Class<? extends T> ingredientClass) {
		//noinspection unchecked
		List<List<T>> inputs = (List<List<T>>) (Object) this.inputs.get(ingredientClass);
		if (inputs == null) {
			return Collections.emptyList();
		}
		return inputs;
	}

	@Override
	public <T> List<List<T>> getOutputs(Class<? extends T> ingredientClass) {
		//noinspection unchecked
		List<List<T>> outputs = (List<List<T>>) (Object) this.outputs.get(ingredientClass);
		if (outputs == null) {
			return Collections.emptyList();
		}
		return outputs;
	}

	public Map<Class, List> getInputIngredients() {
		Map<Class, List> inputIngredients = new IdentityHashMap<Class, List>();
		for (Map.Entry<Class, List<List>> entry : inputs.entrySet()) {
			List<Object> flatIngredients = new ArrayList<Object>();
			for (List ingredients : entry.getValue()) {
				flatIngredients.addAll(ingredients);
			}
			inputIngredients.put(entry.getKey(), flatIngredients);
		}
		return inputIngredients;
	}

	public Map<Class, List> getOutputIngredients() {
		Map<Class, List> outputIngredients = new IdentityHashMap<Class, List>();
		for (Map.Entry<Class, List<List>> entry : outputs.entrySet()) {
			List<Object> flatIngredients = new ArrayList<Object>();
			for (List ingredients : entry.getValue()) {
				flatIngredients.addAll(ingredients);
			}
			outputIngredients.put(entry.getKey(), flatIngredients);
		}
		return outputIngredients;
	}
}
