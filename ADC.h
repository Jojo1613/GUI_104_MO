
void ADC_Init() {

    // FOSC/32; canale AN0=> RA0; GO_DONE stop; ADON abilitato;  
    ADCON0 = 0B10000001;
    __delay_ms(5);
    
    // ADFM giust. sinistra; ADPREF VDD; ADNREF VSS;
    ADCON1 = 0B10000000;
    __delay_ms(5);

     // ADRESL 0x0; 
    ADRESL = 0x00;

    // ADRESH 0x0; 
    ADRESH = 0x00;

}

int ADC_Conv() {
 
    ADCON0bits.GO_nDONE = 1; // Inizio conversione
    
    while (ADCON0bits.GO_nDONE) {  // Attendi fine conversione
    }
    return ((ADRESH<<8)+ADRESL);
}