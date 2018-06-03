import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ruhan on 21/04/18.
 */
public class GeneratorV2 {

    public static void main(String args[]) throws Exception {
        File file = new File("/Users/ruhandosreis/Documents/ESMOCYP-DATA");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        andar(writer);
        corredor(writer);
        sala(writer);
        sensor(writer);

        String str = "<rdf:Description>\n" +
                "        <rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#AllDifferent\"/>\n" +
                "        <owl:distinctMembers rdf:parseType=\"Collection\">";
        writer.write(str);

        andarDescription(writer);
        corredorDesciption(writer);
        salaDescription(writer);
        sensorDescription(writer);

        str += "        </owl:distinctMembers>\n" +
                "    </rdf:Description>\n";

        writer.write(str);
        writer.close();
    }

    private static void sensor(BufferedWriter writer) throws IOException {

        for( int i = 1; i <= 3000; i++ ) {
            String str = "<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/esmocyp#sensor" + i + "\">\n" +
                    "   <rdf:type rdf:resource=\"http://www.semanticweb.org/esmocyp#Sensor\"/>\n" +
                    "   <rdfs:label>sensor" + i + "</rdfs:label>\n" +
                    "</owl:NamedIndividual>\n\n";

            writer.write(str);
        }
    }

    private static void sala(BufferedWriter writer) throws IOException {
        int i = 1;
        int j = 1;

        while( i <= 1000 ) {

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
            writer.write(str);

            i++;
        }
    }

    private static void andar(BufferedWriter writer) throws IOException {
        int i = 1;
        int j = 1;

        while( i <= 100 ) {

            String str = "<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/esmocyp#andar" + i + "\">\n" +
                    "   <rdf:type rdf:resource=\"http://www.semanticweb.org/esmocyp#Sala\"/>\n";

            int k = 1;
            while( k <= 1 ) {
                str += "    <temCorredor rdf:resource=\"http://www.semanticweb.org/esmocyp#corredor" + j + "\"/>\n";
                j++;
                k++;
            }

            str +=  "   <rdfs:label>andar" + i + "</rdfs:label>\n" +
                    "</owl:NamedIndividual>\n\n";

            System.out.println(str);
            writer.write(str);


            i++;
        }
    }

    private static void corredor(BufferedWriter writer) throws IOException {
        int i = 1;
        int j = 1;

        while( i <= 100 ) {

            String str = "<owl:NamedIndividual rdf:about=\"http://www.semanticweb.org/esmocyp#corredor" + i + "\">\n" +
                    "   <rdf:type rdf:resource=\"http://www.semanticweb.org/esmocyp#Corredor\"/>\n";

            int k = 1;
            while( k <= 10 ) {
                str += "    <temSala rdf:resource=\"http://www.semanticweb.org/esmocyp#sala" + j + "\"/>\n";
                j++;
                k++;
            }

            str +=  "   <rdfs:label>corredor" + i + "</rdfs:label>\n" +
                    "</owl:NamedIndividual>\n\n";

            System.out.println(str);
            writer.write(str);


            i++;
        }
    }

    private static void sensorDescription(BufferedWriter writer) throws IOException {
        for( int i = 1; i <= 3000; i++ ) {
            String str = "              <rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#sensor" + i + "\"/>\n";
            System.out.println(str);
            writer.write(str);

        }
    }

    private static void salaDescription(BufferedWriter writer) throws IOException {
        for( int i = 1; i <= 1000; i++ ) {
            String str = "              <rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#sala" + i + "\"/>\n";
            System.out.println(str);
            writer.write(str);

        }
    }

    private static void andarDescription(BufferedWriter writer) throws IOException {
        for( int i = 1; i <= 100; i++ ) {
            String str = "              <rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#andar" + i + "\"/>\n";
            System.out.println(str);
            writer.write(str);

        }
    }

    private static void corredorDesciption(BufferedWriter writer) throws IOException {
        for( int i = 1; i <= 100; i++ ) {
            String str = "              <rdf:Description rdf:about=\"http://www.semanticweb.org/esmocyp#corredor" + i + "\"/>\n";
            System.out.println(str);
            writer.write(str);

        }
    }
}
