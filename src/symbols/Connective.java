package symbols;

import exceptions.InvalidSymbolException;

public class Connective implements Symbol {

    private enum _Connective {
        IMPLIES("->"),
        AND("/\\"),
        OR("\\/"),
        NOT("~");

        private String conn = null;
        _Connective(String conn) {
            this.conn = conn;
        }

        public String toString() {
            return this.conn;
        }

        public boolean equals(String other) {
            return this.conn.equals(other);
        }


        /**
         * Convert the given string to a _Connective object, null if not valid
         *
         * @param str the string to be converted into a connective
         * @return _Connective if it is a valid connective else null
         *         valid connectives: -> or /\ or \/ or ~
         */
        public static _Connective convert(String str) {
            for (_Connective valid_conn : _Connective.values()) {
                if (valid_conn.equals(str)) {
                    return valid_conn;
                }
            }
            return null;
        }

    }

    private _Connective connective;

    public Connective(String str) throws InvalidSymbolException {
        this.connective = _Connective.convert(str);
        if (this.connective == null) {
            throw new InvalidSymbolException("\"%s\" is not a valid connective".format(str));
        }
    }

    @Override
    public boolean equals(String other) {
        return this.connective.equals(other);
    }

    @Override
    public String toString() {
        return this.connective.toString();
    }
}
