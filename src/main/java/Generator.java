/**
 * Created by ruhan on 21/04/18.
 */
public class Generator {

    public static void main(String args[]) {
        sala();
        //sensor();
        //sensorDescription();
        //salaDescription();
    }

    private static void sensor() {
        for( int i = 1; i <= 300; i++ ) {
            String str = "<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/esmocyp#sensor" + i + "\">\n" +
                    "   <rdf:type rdf:resource=\"http://www.semanticweb.org/esmocyp#Sensor\"/>\n" +
                    "   <rdfs:label>sensor" + i + "</rdfs:label>\n" +
                    "</owl:NamedIndividual>\n\n";

            System.out.println(str);
        }
    }

    private static void sala() {
        int i = 1;
        int j = 1;

        while( i <= 100 ) {

            String str = "<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/esmocyp#sala" + i + "\">\n" +
                    "   <rdf:type rdf:resource=\"http://www.semanticweb.org/esmocyp#Sala\"/>\n";

            int k = 1;
            while( k <= 3 ) {
                str += "    <temSensor rdf:resource=\"http://www.semanticweb.org/esmocyp#sensor" + j + "\"/>\n";
                j++;
                k++;
            }

            str +=  "   <estaPertoDe rdf:resource=\"http://www.semanticweb.org/esmocyp#sala1\"/>\n" +
                    "   <rdfs:label>sala" + i + "</rdfs:label>\n" +
                    "</owl:NamedIndividual>\n\n";

            System.out.println(str);

            i++;
        }
    }

    private static void sensorDescription() {
        for( int i = 1; i <= 300; i++ ) {
            String str = "<rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#sensor" + i + "\"/>";
            System.out.println(str);
        }
    }

    private static void salaDescription() {
        for( int i = 1; i <= 100; i++ ) {
            String str = "<rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#sala" + i + "\"/>";
            System.out.println(str);
        }
    }
}
