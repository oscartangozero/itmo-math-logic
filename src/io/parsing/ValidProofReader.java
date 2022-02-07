package io.parsing;

import model.expression.Variable;
import model.proof.Proof;
import model.proof.DraftProof;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ValidProofReader {
    public static Proof read(InputStream stream) throws IOException, ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return ValidProofReader.read(reader);
    }

    public static Proof read(BufferedReader reader) throws IOException, ParseException {
        Map<String, Variable> variablesCache = new HashMap<>();
        String line = reader.readLine();
        DraftProof draftProof = ProofParser.parseProposition(line, variablesCache);
        while ((line = reader.readLine()) != null) {
            draftProof.tryAppendStatement(ProofParser.parseStatement(line, variablesCache));
        }
        Proof proof = draftProof.tryValidate();
        if (proof != null) return proof;
        throw new RuntimeException("Read proof is invalid");
    }
}
