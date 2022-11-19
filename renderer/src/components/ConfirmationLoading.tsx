import React, { useEffect } from "react";
import Link from "next/link";
import { useRouter } from 'next/router';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSync } from '@fortawesome/free-solid-svg-icons';


const ConfirmationLoading = () => {
    const [info, setInfo] = React.useState<string>('Potwierdzanie konta...')
    const [confirming, setConfirming] = React.useState<boolean>(true)
    const [result, setResult] = React.useState<string>('')
    const router = useRouter()
    const {token} = router.query
    
    useEffect(() => {
        if(!token){
            return;
        }
        const delay = (ms: number) => new Promise(r => setTimeout(r, ms));

        const sendConfirmation = async () =>{
            await delay(2000);
            const data = await fetch(`http://localhost:8080/registration/confirm_registration?token=${token}`);
            if (!data.ok) {
                if(data.status == 410){
                    setResult("expired");
                }
                else{
                    setResult("unConfirmed");
                }
            }
            else {
                setResult("confirmed");
            }
            setInfo(await data.text());
            setConfirming(false);
            console.log(result);
        }
        sendConfirmation().catch(e => {setInfo('Wystąpił nieoczekiwany problem'); setConfirming(false); setResult("unConfirmed")})

      }, [token])

    return(
        <div className="h-full max-w-xl min-w-[20rem] w-full bg-base-100 p-8 flex flex-col gap-5 rounded-md">
            <h1 className="text-4xl mb-10 text-center">{info}</h1>
            <div>
                {confirming? <FontAwesomeIcon className="fadeIn fa-spin w-full mb-10" icon={faSync} size="10x" />
                : <FontAwesomeIcon className="fadeIn w-full mb-10" icon={faSync} size="10x" />
                }
            </div>
            {
                result == "confirmed" ?
                    <Link href="/login">
                        <a className="btn btn-primary w-full max-w-xs mx-auto my-2 relative">Zaloguj</a>
                    </Link>
                : result == "unConfirmed" ?
                    <Link href="/register">
                        <a className="btn btn-primary w-full max-w-xs mx-auto my-2 relative">Zarejestruj się</a>
                    </Link>
                : result == "expired" ?
                    <h3 className="text-4xl mb-10 text-center"> Nowy link został wysłany na Twojego emaila</h3>
                :
                <div></div>
            }       
        </div>
    );
};

export default ConfirmationLoading;