package app.controllers.model;

import java.util.ArrayList;
import java.util.List;

public final class Subject 
{
    public static final class Principal 
    {
        String name;
        String fingerprint;
        String signature;
        Boolean remember;
        String inputId;

        public Principal()
        {
            
        }

        public String getName()
        {
            return name;
        }

        public Principal setName(String name)
        {
            this.name = name;
            return this;
        }

        public String getFingerprint()
        {
            return fingerprint;
        }

        public Principal setFingerprint(String fingerprint)
        {
            this.fingerprint = fingerprint;
            return this;
        }


        public String getSignature()
        {
            return signature;
        }

        public Principal setSignature(String signature)
        {
            this.signature = signature;
            return this;
        }
        
        public Boolean getRemember()
        {
            return remember;
        }

        public Principal setRemember(Boolean remember)
        {
            this.remember = remember;
            return this;
        }
        public String getInputId()
        {
            return inputId;
        }

        public Principal setInputId(String inputId)
        {
            this.inputId = inputId;
            return this;
        }

    }  
    public static final class Credential
    {
        public static final class InputMetadata 
        {
            public static final class InputOption 
            {


                public static InputOption getDefaultInstance()
                {
                    return DEFAULT_INSTANCE;
                }

                static final InputOption DEFAULT_INSTANCE = new InputOption();

                String label;
                String value;

                public InputOption()
                {
                    
                }

                // getters and setters

                // label

                public String getLabel()
                {
                    return label;
                }

                public InputOption setLabel(String label)
                {
                    this.label = label;
                    return this;
                }

                // value

                public String getValue()
                {
                    return value;
                }

                public InputOption setValue(String value)
                {
                    this.value = value;
                    return this;
                }
            }

            public enum InputType 
            {
                Text(0),
                Password(1),
                Checkbox(2),
                Image(3),
                Radio(4),
                Select(5),
                Label(6),
                Title(7),
                Link(8);
                
                public final int number;
                
                private InputType (int number)
                {
                    this.number = number;
                }
                
                public int getNumber()
                {
                    return number;
                }
                
                public static InputType valueOf(int number)
                {
                    switch(number) 
                    {
                        case 0: return Text;
                        case 1: return Password;
                        case 2: return Checkbox;
                        case 3: return Image;
                        case 4: return Radio;
                        case 5: return Select;
                        case 6: return Label;
                        case 7: return Title;
                        case 8: return Link;
                        default: return null;
                    }
                }
            }

            InputType type;
            String label;
            String errorMessage;
            List<InputOption> options;

            public InputMetadata()
            {
                
            }



            public InputType getType()
            {
                return type == null ? InputType.Text : type;
            }

            public InputMetadata setType(InputType type)
            {
                this.type = type;
                return this;
            }

            public String getLabel()
            {
                return label;
            }

            public InputMetadata setLabel(String label)
            {
                this.label = label;
                return this;
            }

            public String getErrorMessage()
            {
                return errorMessage;
            }

            public InputMetadata setErrorMessage(String errorMessage)
            {
                this.errorMessage = errorMessage;
                return this;
            }

            public List<InputOption> getOptionsList()
            {
                return options;
            }

            public InputMetadata setOptionsList(List<InputOption> options)
            {
                this.options = options;
                return this;
            }

            public InputOption getOptions(int index)
            {
                return options == null ? null : options.get(index);
            }

            public int getOptionsCount()
            {
                return options == null ? 0 : options.size();
            }

            public InputMetadata addOptions(InputOption options)
            {
                if(this.options == null)
                    this.options = new ArrayList<InputOption>();
                this.options.add(options);
                return this;
            } 
        }

        String name;
        String value;
        InputMetadata metadata;

        public Credential()
        {
            
        }

        public String getName()
        {
            return name;
        }

        public Credential setName(String name)
        {
            this.name = name;
            return this;
        }
        public String getValue()
        {
            return value;
        }

        public Credential setValue(String value)
        {
            this.value = value;
            return this;
        }

        public InputMetadata getMetadata()
        {
            return metadata;
        }

        public Credential setMetadata(InputMetadata metadata)
        {
            this.metadata = metadata;
            return this;
        }


    }


    Principal principal;
    List<Credential> credentials;
    List<Credential> missingCredentials;
    List<FI.Account> accounts;
    String errorMessage;

    public Subject()
    {
        
    }


    public Principal getPrincipal()
    {
        return principal;
    }

    public Subject setPrincipal(Principal principal)
    {
        this.principal = principal;
        return this;
    }


    public List<Credential> getCredentialsList()
    {
        return credentials;
    }

    public Subject setCredentialsList(List<Credential> credentials)
    {
        this.credentials = credentials;
        return this;
    }

    public Credential getCredentials(int index)
    {
        return credentials == null ? null : credentials.get(index);
    }

    public int getCredentialsCount()
    {
        return credentials == null ? 0 : credentials.size();
    }

    public Subject addCredentials(Credential credentials)
    {
        if(this.credentials == null)
            this.credentials = new ArrayList<Credential>();
        this.credentials.add(credentials);
        return this;
    }

    public List<Credential> getMissingCredentialsList()
    {
        return missingCredentials;
    }

    public Subject setMissingCredentialsList(List<Credential> missingCredentials)
    {
        this.missingCredentials = missingCredentials;
        return this;
    }

    public Credential getMissingCredentials(int index)
    {
        return missingCredentials == null ? null : missingCredentials.get(index);
    }

    public int getMissingCredentialsCount()
    {
        return missingCredentials == null ? 0 : missingCredentials.size();
    }

    public Subject addMissingCredentials(Credential missingCredentials)
    {
        if(this.missingCredentials == null)
            this.missingCredentials = new ArrayList<Credential>();
        this.missingCredentials.add(missingCredentials);
        return this;
    }

    public List<FI.Account> getAccountsList()
    {
        return accounts;
    }

    public Subject setAccountsList(List<FI.Account> accounts)
    {
        this.accounts = accounts;
        return this;
    }

    public FI.Account getAccounts(int index)
    {
        return accounts == null ? null : accounts.get(index);
    }

    public int getAccountsCount()
    {
        return accounts == null ? 0 : accounts.size();
    }

    public Subject addAccounts(FI.Account accounts)
    {
        if(this.accounts == null)
            this.accounts = new ArrayList<FI.Account>();
        this.accounts.add(accounts);
        return this;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public Subject setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
        return this;
    }
}