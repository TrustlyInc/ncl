package app.controllers.model;

public final class PaymentProvider 
{
    public enum Type 
    {
        _NULL_PAYMENTPROVIDER_TYPE(-1),
        PayWithMyBank(1),
        ECH(2);
        
        public final int number;
        
        private Type (int number)
        {
            this.number = number;
        }
        
        public int getNumber()
        {
            return number;
        }
        
        public static Type valueOf(int number)
        {
            switch(number) 
            {
                case -1: return _NULL_PAYMENTPROVIDER_TYPE;
                case 1: return PayWithMyBank;
                case 2: return ECH;
                default: return null;
            }
        }
    }


    String paymentProviderId;
    String name;
    String shortName;
    Type type;
	public String getPaymentProviderId() {
		return paymentProviderId;
	}
	public PaymentProvider setPaymentProviderId(String paymentProviderId) {
		this.paymentProviderId = paymentProviderId;
		return this;
	}
	public String getName() {
		return name;
	}
	public PaymentProvider setName(String name) {
		this.name = name;
		return this;
	}
	public String getShortName() {
		return shortName;
	}
	public PaymentProvider setShortName(String shortName) {
		this.shortName = shortName;
		return this;
	}
	public Type getType() {
		return type;
	}
	public PaymentProvider setType(Type type) {
		this.type = type;
		return this;
	}
}
