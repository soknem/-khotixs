import Image from "next/image";
import Link from "next/link";

export default function Home() {
    return (
        <div
            className=" grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
            <div className="mt-[100px]">
                <h1 className="text-[3rem]">Welcome to Khotixs</h1>
                <Link
                    href="/oauth2/authorization/nextjs"
                    className="flex items-center justify-center text-[3rem] font-light text-white rounded-lg p-5 border-2 border-transparent transition-transform transform hover:scale-105 hover:shadow-lg hover:border-[#4e54c8] hover:brightness-90 bg-gradient-to-r from-[#8e44ad] to-[#c39bd3]"
                >
                    Login
                </Link>

            </div>
        </div>
    );
}
