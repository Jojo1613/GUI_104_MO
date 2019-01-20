
/*
http://eng-serve.com/pic/pic_timer.html 
 
 */

void PWM1_Duty(unsigned int duty1) {
    CCP1CONbits.DC1B1 = duty1 & 2;
    CCP1CONbits.DC1B0 = duty1 & 1;
    CCPR1L = duty1>>2;
}
void PWM2_Duty(unsigned int duty2) {
    //faccio una AND con i bit meno significativi così da inserirli nel egistro d'appoggio
    //in qunato il valore è compreso tra 0 e 103, così gli 8 più significativi li metto nel registro normale e i due meno
    //significativi li metto nei due d'appoggio(CCP2CON in questo caso)
    CCP2CONbits.DC2B1 = duty2 & 2;//es. 1000111001 && 00000010
    CCP2CONbits.DC2B0 = duty2 & 1;//es. 1000111001 && 00000001
                                  
    CCPR2L = duty2>>2;//faccio uno shift per impostare il registro CCPR2L con i bit più significativi
}

void PWM_Duty(unsigned int duty1,unsigned int duty2){
    if(duty1<1024)     PWM1_Duty(duty1);
    if(duty2<1024)     PWM2_Duty(duty2);
}

void PWM1_on()				// PWM1 su (RC2)
{
  CCP1CONbits.CCP1M1=0;     //singola uscita del PWM su P1A (RC2)
  CCP1CONbits.CCP1M0=0;    
  
  CCP1CONbits.CCP1M2 = 1;
  CCP1CONbits.CCP1M3 = 1;
}

void PWM1_off()
{
  CCP1CONbits.CCP1M2 = 0;	// PWM2 su (RC1)
  CCP1CONbits.CCP1M3 = 0;
}

void PWM2_on()
{
  CCP2CONbits.CCP2M3 = 1;
  CCP2CONbits.CCP2M2 = 1;
}

void PWM2_off()
{
  CCP2CONbits.CCP2M3 = 0;
  CCP2CONbits.CCP2M2 = 0;
}

void PWM_Init(int prescTMR2) {
CCP1CON=0;                      //resetto il modulo CCP1
CCP2CON=0;                      //resetto il modulo CCP2

PR2=0xFF;                       //fisso PR2 al max valore

PWM_Duty(500,500);              //DC 50% per PWM1 e PWM2

  if (prescTMR2 == 1) {         // imposto il prescaler det TIMER2 comune ai due moduli PWM
    T2CONbits.T2CKPS0 = 0;
    T2CONbits.T2CKPS1 = 0;
    }
  else if (prescTMR2 == 4){
    T2CONbits.T2CKPS0 = 1;
    T2CONbits.T2CKPS1 = 0;
    }
  else if (prescTMR2 == 16){
    T2CONbits.T2CKPS0 = 1;
    T2CONbits.T2CKPS1 = 1;
    }

  T2CONbits.TMR2ON = 1;         // faccio partire il TIMER2
  

PWM1_on();                      // avvio il PWM1 su RC2
PWM2_on();                      // avvio il PWM2 su RC1

}