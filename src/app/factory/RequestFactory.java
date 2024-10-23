package app.factory;

import app.models.*;
import ast.base.ABase;
import ast.bexp.*;
import ast.cexp.*;
import ast.cont.*;
import ast.dirs.*;
import ast.gather.*;
import ast.query.*;
import ast.rand.*;
import fr.sorbonne_u.cps.sensor_network.interfaces.Direction;
import fr.sorbonne_u.cps.sensor_network.interfaces.RequestI;
import fr.sorbonne_u.cps.sensor_network.requests.interfaces.QueryI;

/**
 * Provides a static factory method for creating different types of query
 * requests for sensor network operations. Each request is tailored to specific
 * sensor data gathering, controlling, or querying tasks using various
 * components of the abstract syntax tree (AST) related to sensor network
 * configurations.
 */
public abstract class RequestFactory {

	/**
	 * Enumerates possible types of requests that can be created, ranging from
	 * simple data gathering to complex querying with specific control and data
	 * operations.
	 */
	public enum RequestType {
		SIMPLE, FCONT_BASED, DCONT, GQUERY_WITH_ECONT, BQUERY_SIMPLE, FCONT_WITH_DCONT, GQUERY_COMPLEX, BQUERY_COMPLEX,
		ECONT_WITH_RGATHER, FCONT_WITH_MULTIPLE_SENSORS, DCONT_COMPLEX, BQUERY_DYNAMIC, CUSTOM_COMPLEX_QUERY, BQUERY_OR,
		BQUERY_AND_VERGLAS, BQUERY_AND_NOT_VERGLAS, BQUERY_VERIFY_HUMIDITY, VERIF_TEMP_SEUIL
	}

	/**
	 * Creates a request of the specified type with provided parameters. This method
	 * serves as a centralized point to generate different kinds of requests based
	 * on AST components and sensor network configurations.
	 *
	 * @param params Varargs parameter to pass required details like request type,
	 *               sensor identifiers, positions, directions, and other necessary
	 *               parameters based on the request type.
	 * @return A {@link RequestI} object configured as per the specified parameters.
	 * @throws IllegalArgumentException if the request type is unknown or parameters
	 *                                  are incorrect.
	 */
	public static RequestI createRequest(Object... params) {
		RequestType type = (RequestType) params[0];
		switch (type) {
		case SIMPLE:
			EnumSensorIdentifier sensorIdSimple = (EnumSensorIdentifier) params[1];
			return new Request(new GQuery(new FGather(sensorIdSimple.name()), new ECont()), null);
		case FCONT_BASED:
			Position position = (Position) params[1];
			int threshold = (Integer) params[2];
			EnumSensorIdentifier sensorIdFCont = (EnumSensorIdentifier) params[3];
			return new Request(new GQuery(new FGather(sensorIdFCont.name()), new FCont(new ABase(position), threshold)),
					null);
		case DCONT:
			Direction direction = (Direction) params[1];
			int depth = (Integer) params[2];
			EnumSensorIdentifier sensorIdDCont = (EnumSensorIdentifier) params[3];
			return new Request(new GQuery(new FGather(sensorIdDCont.name()),
					new DCont(new Rdirs(direction, new Fdirs(Direction.NW)), depth)), null);
		case BQUERY_COMPLEX:
			Direction direction1 = (Direction) params[1];
			double heatLevel = (Double) params[2];
			double smokeLevel = (Double) params[3];
			int dirsDepth = (Integer) params[4];
			EnumSensorIdentifier sensorIdHeat = (EnumSensorIdentifier) params[5];
			EnumSensorIdentifier sensorIdSmoke = (EnumSensorIdentifier) params[6];
			return new Request(new BQuery(
					new AndBExp(new CExpBExp(new GeqCExp(new SRand(sensorIdHeat.name()), new CRand(heatLevel))),
							new CExpBExp(new GeqCExp(new SRand(sensorIdSmoke.name()), new CRand(smokeLevel)))),
					new DCont(new Fdirs(direction1), dirsDepth)), null);
		case GQUERY_COMPLEX:
			int distance = (Integer) params[1];
			Position position2 = (Position) params[2];
			EnumSensorIdentifier sensorIdComplex1 = (EnumSensorIdentifier) params[3];
			EnumSensorIdentifier sensorIdComplex2 = (EnumSensorIdentifier) params[4];
			EnumSensorIdentifier sensorIdComplex3 = (EnumSensorIdentifier) params[5];
			return new Request(new GQuery(
					new RGather(sensorIdComplex1.name(),
							new RGather(sensorIdComplex2.name(), new FGather(sensorIdComplex3.name()))),
					new FCont(new ABase(position2), distance)), null);
		case BQUERY_OR:
			double valeurPlusGrandeQueSensor = (Double) params[1];
			double valeurPlusPetiteQueSensor = (Double) params[2];
			EnumSensorIdentifier type1 = (EnumSensorIdentifier) params[5];
			EnumSensorIdentifier type2 = (EnumSensorIdentifier) params[6];
			Position position3 = (Position) params[3];
			int distance2 = (Integer) params[4];
			return new Request((QueryI) new BQuery(
					new OrBExp(new CExpBExp(new GCExp(new SRand(type1.name()), new CRand(valeurPlusGrandeQueSensor))),
							new CExpBExp(new LCExp(new CRand(valeurPlusPetiteQueSensor), new SRand(type2.name())))),
					new FCont(new ABase(position3), distance2)), null);
		case ECONT_WITH_RGATHER:
			EnumSensorIdentifier primarySensor = (EnumSensorIdentifier) params[1];
			EnumSensorIdentifier secondarySensor = (EnumSensorIdentifier) params[2];
			return new Request(
					new GQuery(new RGather(primarySensor.name(), new FGather(secondarySensor.name())), new ECont()),
					null);

		case FCONT_WITH_MULTIPLE_SENSORS:
			Position pos = (Position) params[1];
			int range = (Integer) params[2];
			EnumSensorIdentifier sensor1 = (EnumSensorIdentifier) params[3];
			EnumSensorIdentifier sensor2 = (EnumSensorIdentifier) params[4];
			return new Request(new GQuery(new RGather(sensor1.name(), new FGather(sensor2.name())),
					new FCont(new ABase(pos), range)), null);

		case DCONT_COMPLEX:
			Direction dir = (Direction) params[1];
			int maxDepth = (Integer) params[2];
			EnumSensorIdentifier complexSensor1 = (EnumSensorIdentifier) params[3];
			EnumSensorIdentifier complexSensor2 = (EnumSensorIdentifier) params[4];
			return new Request(new BQuery(
					new OrBExp(new CExpBExp(new LeqCExp(new SRand(complexSensor1.name()), new CRand(50.0))),
							new CExpBExp(new GeqCExp(new SRand(complexSensor2.name()), new CRand(10.0)))),
					new DCont(new Rdirs(dir, new Fdirs(Direction.SE)), maxDepth)), null);

		case BQUERY_DYNAMIC:
			Direction dynamicDir = (Direction) params[1];
			EnumSensorIdentifier dynamicSensor = (EnumSensorIdentifier) params[2];
			double criticalLevel = (Double) params[3];
			return new Request(
					new BQuery(new CExpBExp(new GeqCExp(new SRand(dynamicSensor.name()), new CRand(criticalLevel))),
							new DCont(new Rdirs(dynamicDir, new Fdirs(Direction.NW)), 3)),
					null);

		case CUSTOM_COMPLEX_QUERY:
			EnumSensorIdentifier customSensor1 = (EnumSensorIdentifier) params[1];
			EnumSensorIdentifier customSensor2 = (EnumSensorIdentifier) params[2];
			Position customPos = (Position) params[3];
			int customThreshold = (Integer) params[4];
			return new Request(new GQuery(new RGather(customSensor1.name(), new FGather(customSensor2.name())),
					new FCont(new ABase(customPos), customThreshold)), null);
		case VERIF_TEMP_SEUIL:
			double seuil = (Double) params[1];
			EnumSensorIdentifier toVerify = (EnumSensorIdentifier) params[2];
			Position position4 = (Position) params[3];
			int distanceMax = (Integer) params[4];
			return new Request(new BQuery(new CExpBExp(new LCExp(new SRand(toVerify.name()), new CRand(seuil))),
					new FCont(new ABase(position4), distanceMax)), null);
		case BQUERY_AND_VERGLAS:
			Position p = (Position) params[1];
			int distanceHumidite = (Integer) params[2];
			return new Request(new BQuery(
					new AndBExp(
							new CExpBExp(new LeqCExp(new SRand(EnumSensorIdentifier.Heat.name()), new CRand(273.15))),
							new CExpBExp(
									new GeqCExp(new SRand(EnumSensorIdentifier.Humidity.name()), new CRand(60.0)))),
					new FCont(new ABase(p), distanceHumidite)), null);
		case BQUERY_AND_NOT_VERGLAS:
			Position p2 = (Position) params[1];
			int distanceHumidite2 = (Integer) params[2];
			return new Request(new BQuery(
					new AndBExp(
							new NotBExp(new CExpBExp(
									new GCExp(new SRand(EnumSensorIdentifier.Heat.name()), new CRand(273.15)))),
							new CExpBExp(
									new GeqCExp(new SRand(EnumSensorIdentifier.Humidity.name()), new CRand(60.0)))),
					new FCont(new ABase(p2), distanceHumidite2)), null);

		case BQUERY_VERIFY_HUMIDITY:
			Position p3 = (Position) params[1];
			int distanceHumidite3 = (Integer) params[2];
			double seuilHumidity = (double) params[3];
			return new Request(new BQuery(
					new CExpBExp(new LCExp(new SRand(EnumSensorIdentifier.Humidity.name()), new CRand(seuilHumidity))),
					new FCont(new ABase(p3), distanceHumidite3)), null);

		default:
			throw new IllegalArgumentException("Unknown request type: " + type);
		}
	}

}
