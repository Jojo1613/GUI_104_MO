
#define ADDR_SP 0b01000000        //indirizzo del CI MCP23S17 e comando Write

#define GPIO_DIR_A 0x00           //indirizzo di IODIRA
#define GPIO_A 0x12               //indrizzo registro GPIOA
#define OLAT_A 0x14               //indirizzo registro OLATB 

#define GPIO_DIR_B 0x01           //indirizzo di IODIRB  
#define GPIO_B 0x13               //indirizzo registro GPIOB 
#define OLAT_B 0x15               //indirizzo registro OLATB 


void SPI_Init()         //inizializzo la periferica SPI
{
 
   // BF RCinprocess_TXcomplete; UA dontupdate; SMP Sample At Middle; P stopbit_notdetected; S startbit_notdetected; R_nW write_noTX; CKE Active to Idle; D_nA lastbyte_address; 
    SSPSTAT = 0b01000000;

    // SSPEN enabled; WCOL no_collision; SSPOV no_overflow; CKP Idle:Low, Active:High; SSPM FOSC/64; 
    SSPCON = 0b00100010;
    
    SSPCON2=0x00;  
        
}

void spi_port(char dato)   //invio dato alla periferica SPI
{
    SSPCONbits.WCOL = 0;  //reset del flag di collisione

    SSPBUF = dato;         //carico dato nel buffer SPI
    
    while (SSPSTATbits.BF == 0) {    //aspetto che il dato sia inviato
    }
    
}

void Lcd_SP_port(char reg, char val) // trasmissione del dato seriale
{
	PORTAbits.RA2=0;        //abilito il ChipSelect del MCP23S17
    
    spi_port(ADDR_SP);      //invio indirizzo del CI MCP23S17
    spi_port(reg);          //invio indirizzo del registro
    spi_port(val);          //invio valore da scrivere  
    
    PORTAbits.RA2=1;        //disabilito il ChipSelect del MCP23S17
}


void Lcd_SP_Cmd(char cmd)
{
	Lcd_SP_port(GPIO_A,0x00); //RS=0 -- E=0 
    Lcd_SP_port(GPIO_B,cmd);  //scrivo il comando sulla porta B
	Lcd_SP_port(GPIO_A,0x40); //RS=0 -- E=1     
    Lcd_SP_port(GPIO_A,0x00); //RS=0 -- E=0
}
// per abilitare LCD settare anche bit 5 di GPIOA



void Lcd_SP_Clear()             // Cancella LCD
{
	Lcd_SP_Cmd(1);
	Lcd_SP_Cmd(2);
}

void Lcd_SP_Set_Cursor(char riga, char colonna)     // Posiziona cursore
{   
    char pos;
    
    if(riga<=2 && riga>0)
	{
	  pos = (riga == 1) ? (0x80 | (colonna-1)) : (0xC0 | (colonna-1));
      Lcd_SP_Cmd(pos);
	}
}


void Lcd_SP_Init()
{
   SPI_Init(); 
   
   RA2=0;                  // abilito il CI MCP23S17
   RB5=0;                  // e lo resetto
   __delay_ms(10);
   RB5=1;
   RA2=1;     
    
   Lcd_SP_port(GPIO_DIR_A, 0x00); //configuro GPIOA out 
   Lcd_SP_port(GPIO_DIR_B , 0x00); //configuro GPIOB out  
 
   __delay_ms(10);      
   Lcd_SP_Cmd(0x3C);
   __delay_ms(10);
   Lcd_SP_Cmd(0x0C);
   __delay_ms(10);
 
   Lcd_SP_Cmd(0x01); 
   __delay_ms(10);
   Lcd_SP_Cmd(0x0C);
 
   __delay_ms(130);    
   Lcd_SP_Cmd(0x80);
   __delay_ms(10);
    
}

void Lcd_SP_Write_Char(char caratt)
{
    Lcd_SP_port(GPIO_A,0x80); //RS=1 -- E=0 
    Lcd_SP_port(GPIO_B,caratt);    //scrivo il carattere sulla porta B
	Lcd_SP_port(GPIO_A,0xC0); //RS=1 -- E=1     
    Lcd_SP_port(GPIO_A,0x80); //RS=1 -- E=0
}


void Lcd_SP_Write_String(char *b)
{
	int i;
	for(i=0;b[i]!='\0';i++)
	   Lcd_SP_Write_Char(b[i]);
}


void Lcd_SP_Shift_Right()
{
	Lcd_SP_Cmd(0x1C);
}

void Lcd_SP_Shift_Left()
{
	Lcd_SP_Cmd(0x18);
}

void Lcd_SP_Write_Num(char val) 
{
    char dec=0, unit=0;

 //scomposizione numero in unità e decine 
    
    while (val>=10) {
        val-=10;
        dec++;
        }
    unit=val;
    
    Lcd_SP_Write_Char(dec+=48);    
    Lcd_SP_Write_Char(unit+=48);        
 
}

void Lcd_SP_Write_Val(int val) 
{
    char cent=0, dec=0, uni=0;

 //scomposizione di 'val' in unità decine e centinaia
    
    while (val>=100) { 
        val-=100;
        cent++;
        }
    while (val>=10) {
        val-=10;
        dec++;
        }
    uni=val;
    
    if (cent==0 && dec==0) dec=32; // spengo 'dec' se 'cent' e 'dec' valgono '0'
        else dec+=48;
    
    if (cent==0) cent=32; // spengo 'cent' se vale '0'
        else cent+=48;
    
    uni+=48;
    
    Lcd_SP_Write_Char(cent);
    Lcd_SP_Write_Char(dec);    
    Lcd_SP_Write_Char(uni);        
 
}

void Lcd_SP_Write_Volt(char val) 
{
    float num=0, cifre=0, dec=0, cent=0;
    
    num=val*0.019607843;
    
    Lcd_SP_Write_Char(num+=48);  
    
    num*=100;
    
    while (num>=100) { 
        num-=100;
        }
   
    cifre=num;
    
    while (cifre>=10) { 
        cifre-=10;
        dec++;
        }
    cent=cifre;
    
    Lcd_SP_Write_String(".");  
    Lcd_SP_Write_Char(dec+=48); 
    Lcd_SP_Write_Char(cent+=48); 
}
