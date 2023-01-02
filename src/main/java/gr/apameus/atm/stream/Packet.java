package gr.apameus.atm.stream;

public interface Packet {

    byte LOGOUT = 0;
    byte REGISTER = 1;
    byte LOGIN = 2;
    byte DEPOSIT = 3;
    byte WITHDRAW = 4;
    byte TRANSFER = 5;
    byte ERROR = 6;
    byte SUCCESS = 7;

    byte CREDIT_CARD = 8;
    byte BALANCE = 9;

    byte type();

    record RegisterPacket(String cardNumber, String cardPin) implements Packet {
        @Override
        public byte type() {
            return REGISTER;
        }
    }

    record LoginPacket(String cardNumber, String cardPin) implements Packet {
        @Override
        public byte type() {
            return LOGIN;
        }
    }

    record DepositPacket(String cardNumber, Double amount) implements Packet {
        @Override
        public byte type() {
            return DEPOSIT;
        }
    }

    record WithdrawPacket(String cardNumber ,Double amount) implements Packet {
        @Override
        public byte type() {
            return WITHDRAW;
        }
    }

    record TransferPacket(String cardNumber ,String transferTo, Double amount) implements Packet {
        @Override
        public byte type() {
            return TRANSFER;
        }
    }

    record ErrorPacket(String message)implements Packet{
        @Override
        public byte type() {
            return ERROR;
        }
    }

    record SuccessPacket(String message) implements Packet{
        @Override
        public byte type() {
            return SUCCESS;
        }
    }

    record CreditCardPacket(String cardNumber, String cardPin ,Double balance) implements Packet{
        @Override
        public byte type() {
            return CREDIT_CARD;
        }
    }

    record BalancePacket(Double balance) implements  Packet{
        @Override
        public byte type() {
            return BALANCE;
        }
    }
}
