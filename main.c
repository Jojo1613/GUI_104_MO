/// Generazione di un segnale PWM su CCP1 e CCP2

#pragma config FOSC = XT        // Oscillator Selection bits (XT oscillator: Crystal/resonator on RA6/OSC2/CLKOUT and RA7/OSC1/CLKIN)
#pragma config WDTE = OFF       // Watchdog Timer Enable bit (WDT disabled and can be enabled by SWDTEN bit of the WDTCON register)
#pragma config PWRTE = OFF      // Power-up Timer Enable bit (PWRT disabled)
#pragma config MCLRE = ON       // RE3/MCLR pin function select bit (RE3/MCLR pin function is MCLR)
#pragma config CP = OFF         // Code Protection bit (Program memory code protection is disabled)
#pragma config CPD = OFF        // Data Code Protection bit (Data memory code protection is disabled)
#pragma config BOREN = OFF      // Brown Out Reset Selection bits (BOR disabled)
#pragma config IESO = OFF       // Internal External Switchover bit (Internal/External Switchover mode is disabled)
#pragma config FCMEN = OFF      // Fail-Safe Clock Monitor Enabled bit (Fail-Safe Clock Monitor is disabled)
#pragma config LVP = OFF        // Low Voltage Programming Enable bit (RB3 pin has digital I/O, HV on MCLR must be used for programming)

// CONFIG2
#pragma config BOR4V = BOR40V   // Brown-out Reset Selection bit (Brown-out Reset set to 4.0V)
#pragma config WRT = OFF        // Flash Program Memory Self Write Enable bits (Write protection off)



#include <xc.h>                    //Header file generico

#define _XTAL_FREQ 8000000         //Specifico la frequenza dell'oscillatore

#include "PWM_1.h"
#include "LCD_SP.h"
#include "ADC.h"
void main()
{
    
    PORTA = 0x00;
    PORTB = 0x00;    
    PORTC = 0x00;
    PORTD = 0x00;    
    PORTE = 0x00;   

    TRISA = 0b00000001;             //Imposto 
    TRISB = 0b00000000;             //Imposto  
    TRISC = 0b00000000;             //Imposto RC2=out per PWM1 e RC1=out per PWM2
    TRISD = 0b00001111;             //Imposto RD3-RD0 come input
    TRISE = 0b00000000;             //Imposto tutti i pin di PORTE come output
    
    ANSEL = 0x01;                   //Imposto tutti i pin come ingressi digitali
    ANSELH = 0x00;                  //Imposto tutti i pin come ingressi digitali    
     
    Lcd_SP_Init();
    ADC_Init();
    
    unsigned int i=500,j=500;
    
    
    PWM_Init(16);          // inizializzo le periferiche PWM1 e PWM2 
                          // impostando il prescaler del TMR2 al valore 16 con PR2=0xFF
                          // Valori ammessi: 1 => 7843.14Hz
                          //                 4 => 1960.78Hz
                          //                 16 => 490.20Hz
   Lcd_SP_Clear();
   __delay_ms(500);
   
  do
  {
      
      Lcd_SP_Set_Cursor(1,1);
      Lcd_SP_Write_String("valore =");
      unsigned int val;
        val=ADC_Conv();
        //val=ADRESH;
        Lcd_SP_Write_Val(val);
        __delay_ms(200);
        PWM2_off();
    /*
    if(RD0 == 0 && i<1010){
      __delay_ms(50);
      if (RD0 == 0 && i<1010){
         i=i+10;
           }
    }
     
    if(RD1 == 0 && i>10){
      __delay_ms(50);
      if (RD1 == 0 && i>10){
         i=i-10;
           }
    }
    if(RD2 == 0 && j<1010){
      __delay_ms(50);
      if (RD2 == 0 && j<1010){
         j=j+10;
           }
    }
     
    if(RD3 == 0 && j>10){
      __delay_ms(50);
      if (RD3 == 0 && j>10){
         j=j-10;
           }
    }*/
   
    PWM1_Duty(val);
 
    __delay_ms(50);
    
  }while(1);
    
}  
