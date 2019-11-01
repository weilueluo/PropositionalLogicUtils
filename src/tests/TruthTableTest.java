package tests;

import core.TruthTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruthTableTest {

    private String ls = System.lineSeparator();
    private TruthTable truth_table = new TruthTable();

    @Test
    void testGenerateImpliesTruthTable() {
        truth_table.evaluate("a -> b");
        assertEquals(truth_table.generate(),
            "|----------|----------|---------------|" + ls +
                    "|         a|         b|    Truth Value|" + ls +
                    "|----------|----------|---------------|" + ls +
                    "|      true|      true|           true|" + ls +
                    "|      true|     false|          false|" + ls +
                    "|     false|      true|           true|" + ls +
                    "|     false|     false|           true|" + ls +
                    "|----------|----------|---------------|" + ls);
    }

    @Test
    void testGenerateOrTruthTable() {
        truth_table.evaluate("a \\/ b");
        assertEquals(truth_table.generate(),
                "|----------|----------|---------------|" + ls +
                        "|         a|         b|    Truth Value|" + ls +
                        "|----------|----------|---------------|" + ls +
                        "|      true|      true|           true|" + ls +
                        "|      true|     false|           true|" + ls +
                        "|     false|      true|           true|" + ls +
                        "|     false|     false|          false|" + ls +
                        "|----------|----------|---------------|" + ls);
    }

    @Test
    void testGenerateAndTruthTable() {
        truth_table.evaluate("a /\\ b");
        assertEquals(truth_table.generate(),
                "|----------|----------|---------------|" + ls +
                        "|         a|         b|    Truth Value|" + ls +
                        "|----------|----------|---------------|" + ls +
                        "|      true|      true|           true|" + ls +
                        "|      true|     false|          false|" + ls +
                        "|     false|      true|          false|" + ls +
                        "|     false|     false|          false|" + ls +
                        "|----------|----------|---------------|" + ls);
    }

    @Test
    void testGenerateIffTruthTable() {
        truth_table.evaluate("a <-> b");
        assertEquals(truth_table.generate(),
                "|----------|----------|---------------|" + ls +
                        "|         a|         b|    Truth Value|" + ls +
                        "|----------|----------|---------------|" + ls +
                        "|      true|      true|           true|" + ls +
                        "|      true|     false|          false|" + ls +
                        "|     false|      true|          false|" + ls +
                        "|     false|     false|           true|" + ls +
                        "|----------|----------|---------------|" + ls);
    }

    @Test
    void testGenerateLargeTruthTable() {
        truth_table.evaluate("(((~(a /\\ (~(~b) \\/ (a))) \\/ (~(c \\/ b) \\/ (~a /\\ ((~(c) \\/ (a)) /\\ (~(a) \\/ " +
                "(c)))))) /\\ (~(~(c \\/ b) \\/ (~a /\\ (~(c) \\/ (a)) /\\ (~(a) \\/ (c)))) \\/ (a /\\ ~(~b) \\/ (a))" +
                ")) \\/ ~b)");
        assertEquals(truth_table.generate(),
                "|----------|----------|----------|---------------|" + ls +
                        "|         a|         b|         c|    Truth Value|" + ls +
                        "|----------|----------|----------|---------------|" + ls +
                        "|      true|      true|      true|          false|" + ls +
                        "|      true|      true|     false|          false|" + ls +
                        "|      true|     false|      true|           true|" + ls +
                        "|      true|     false|     false|           true|" + ls +
                        "|     false|      true|      true|           true|" + ls +
                        "|     false|      true|     false|          false|" + ls +
                        "|     false|     false|      true|           true|" + ls +
                        "|     false|     false|     false|           true|" + ls +
                        "|----------|----------|----------|---------------|" + ls);
        truth_table.evaluate("((a /\\ (~b -> a) <-> c \\/ b -> ~a /\\ (c <-> a)) \\/ ~b)");
        assertEquals(truth_table.generate(),
                "|----------|----------|----------|---------------|" + ls +
                        "|         a|         b|         c|    Truth Value|" + ls +
                        "|----------|----------|----------|---------------|" + ls +
                        "|      true|      true|      true|          false|" + ls +
                        "|      true|      true|     false|          false|" + ls +
                        "|      true|     false|      true|           true|" + ls +
                        "|      true|     false|     false|           true|" + ls +
                        "|     false|      true|      true|           true|" + ls +
                        "|     false|      true|     false|          false|" + ls +
                        "|     false|     false|      true|           true|" + ls +
                        "|     false|     false|     false|           true|" + ls +
                        "|----------|----------|----------|---------------|" + ls);
    }
}