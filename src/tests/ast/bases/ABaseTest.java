package tests.ast.bases;

import app.models.Position;
import ast.base.ABase;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.ExecutionStateI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class ABaseTest {

	@Test
	public void testSerialization() {
		Position position = new Position(1.0, 2.0);
		ABase aBase = new ABase(position);

		try {
			// Serialize
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(aBase);
			oos.close();

			// Deserialize
			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(in);
			ABase deserializedABase = (ABase) ois.readObject();

			assertEquals(aBase.eval(null), deserializedABase.eval(null));
		} catch (IOException | ClassNotFoundException e) {
			fail("Exception should not be thrown during serialization or deserialization");
		}
	}

	@Test
	public void testEval() {
		Position position = new Position(3.0, 4.0);
		ABase aBase = new ABase(position);

		ExecutionStateI mockExecutionState = null;

		Position result = (Position) aBase.eval(mockExecutionState);

		assertNotNull(result);
		assertTrue(result instanceof Position);
		assertEquals(3.0, result.getx());
		assertEquals(4.0, result.gety());
		assertEquals(new Position(3.0, 4.0), result);
	}
}
