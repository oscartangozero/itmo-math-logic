import io.formatting.FormatException;
import io.formatting.ProofFormatter;
import io.parsing.ParseException;
import io.parsing.ValidProofReader;
import model.proof.ConditionalProofBuilder;
import model.proof.Proof;

import java.io.IOException;

public class TaskC {
    public static void main(String[] args) throws IOException, ParseException, FormatException {
        Proof proof = ValidProofReader.read(System.in);
        Proof deducted = ConditionalProofBuilder.build(proof, proof.getHypotheses().length - 1);
        String output = ProofFormatter.format(deducted);
        System.out.println(output);
    }
}
