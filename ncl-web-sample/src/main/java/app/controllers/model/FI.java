package app.controllers.model;

public final class FI 
{
    public static final class Address 
    {
 
        String address1;
        String address2;
        String city;
        String state;
        String zip;
        String country;

        public Address()
        {
            
        }

        public String getAddress1()
        {
            return address1;
        }

        public Address setAddress1(String address1)
        {
            this.address1 = address1;
            return this;
        }

        public String getAddress2()
        {
            return address2;
        }

        public Address setAddress2(String address2)
        {
            this.address2 = address2;
            return this;
        }

        public String getCity()
        {
            return city;
        }

        public Address setCity(String city)
        {
            this.city = city;
            return this;
        }

        public String getState()
        {
            return state;
        }

        public Address setState(String state)
        {
            this.state = state;
            return this;
        }

        public String getZip()
        {
            return zip;
        }

        public Address setZip(String zip)
        {
            this.zip = zip;
            return this;
        }

        public String getCountry()
        {
            return country;
        }

        public Address setCountry(String country)
        {
            this.country = country;
            return this;
        }
    }
    public static final class Account
    {

        String currency = null;
        String amount;

        public Account()
        {
            
        }

        public String getCurrency()
        {
            return currency;
        }

        public Account setCurrency(String currency)
        {
            this.currency = currency;
            return this;
        }

        // amount

        public String getAmount()
        {
            return amount;
        }

        public Account setAmount(String amount)
        {
            this.amount = amount;
            return this;
        }

    }

}
