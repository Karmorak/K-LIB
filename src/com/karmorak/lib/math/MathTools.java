package com.karmorak.lib.math;

public class MathTools {
	
	
	
	public static int getZeroDigits(int number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 1;
		        }
		    } else {
		        if (number < 1000) {
		            return 2;
		        } else {
		            if (number < 10000) {
		                return 3;
		            } else {
		                return 4;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 5;
		        } else {
		            return 6;
		        }
		    } else {
		        if (number < 100000000) {
		            return 7;
		        } else {
		            if (number < 1000000000) {
		                return 8;
		            } else {
		                return 9;
		            }
		        }
		    }
		}
	}
	
	public static int getZeroDigits(float number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 1;
		        }
		    } else {
		        if (number < 1000) {
		            return 2;
		        } else {
		            if (number < 10000) {
		                return 3;
		            } else {
		                return 4;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 5;
		        } else {
		            return 6;
		        }
		    } else {
		        if (number < 100000000) {
		            return 7;
		        } else {
		            if (number < 1000000000) {
		                return 8;
		            } else {
		                return 9;
		            }
		        }
		    }
		}
	}
	
	public static int getZeroDigits(double number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 1;
		        }
		    } else {
		        if (number < 1000) {
		            return 2;
		        } else {
		            if (number < 10000) {
		                return 3;
		            } else {
		                return 4;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 5;
		        } else {
		            return 6;
		        }
		    } else {
		        if (number < 100000000) {
		            return 7;
		        } else {
		            if (number < 1000000000) {
		                return 8;
		            } else {
		                return 9;
		            }
		        }
		    }
		}
	}
	
	//if a number as two digits like 69 it return 10 by 9421 it returns 1000
	public static int getSmallestPlacevalue(int number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 10;
		        }
		    } else {
		        if (number < 1000) {
		            return 100;
		        } else {
		            if (number < 10000) {
		                return 1000;
		            } else {
		                return 10000;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 100000;
		        } else {
		            return 1000000;
		        }
		    } else {
		        if (number < 100000000) {
		            return 10000000;
		        } else {
		            if (number < 1000000000) {
		                return 100000000;
		            } else {
		                return 1000000000;
		            }
		        }
		    }
		}
	}
	
	public static int getMinPlacevalue(float number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 10;
		        }
		    } else {
		        if (number < 1000) {
		            return 100;
		        } else {
		            if (number < 10000) {
		                return 1000;
		            } else {
		                return 10000;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 100000;
		        } else {
		            return 1000000;
		        }
		    } else {
		        if (number < 100000000) {
		            return 10000000;
		        } else {
		            if (number < 1000000000) {
		                return 100000000;
		            } else {
		                return 1000000000;
		            }
		        }
		    }
		}
	}
	
	public static int getSmallestPlacevalue(double number) {
		if (number < 100000) {
		    if (number < 100) {
		        if (number < 10) {
		            return 0;
		        } else {
		            return 10;
		        }
		    } else {
		        if (number < 1000) {
		            return 100;
		        } else {
		            if (number < 10000) {
		                return 1000;
		            } else {
		                return 10000;
		            }
		        }
		    }
		} else {
		    if (number < 10000000) {
		        if (number < 1000000) {
		            return 100000;
		        } else {
		            return 1000000;
		        }
		    } else {
		        if (number < 100000000) {
		            return 10000000;
		        } else {
		            if (number < 1000000000) {
		                return 100000000;
		            } else {
		                return 1000000000;
		            }
		        }
		    }
		}
	}
	
    // function to find place value(Stellenwert)
    static int placeValue(int N, int D) {
        int total = 1, value = 0, rem = 0;
        while (true) {
            rem = N % 10;
            N = N / 10;
 
            if (rem == D) {
                value = total * rem;
                break;
            }
 
            total = total * 10;
        }
        return value;
    }

}
