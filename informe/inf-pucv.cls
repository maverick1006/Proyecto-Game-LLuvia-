%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% --------------------------- %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% | Plantilla LaTeX         | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% | Formato escuela         | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% | INF PUCV                | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% | por Sebastian Garcia    | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% | @sebaignacioo           | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% |                         | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% |          2023           | %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%% --------------------------- %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesClass{inf-pucv}[2023/04/24 sgarciad.me v0.1, Clase para la plantilla LaTeX con formato escuela INF PUCV.]

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{extarticle}}
\ProcessOptions\relax

\LoadClass{extarticle}

\RequirePackage[T1]{fontenc}
\RequirePackage{lmodern}
\RequirePackage{graphicx}
\RequirePackage{styles}
\RequirePackage{accsupp}
%\RequirePackage{minted}
\RequirePackage{csquotes}
\RequirePackage{hyperref}
%\RequirePackage[style=apa, citestyle=apa, backend=biber]{biblatex}
%\addbibresource{referencias.bib}

\IfFileExists{fontawesome5.sty}{\RequirePackage[fixed]{fontawesome5}}{}
\RequirePackage{ifxetex,ifluatex}
\RequirePackage{scrlfile}
\RequirePackage{xparse}
\RequirePackage{titlesec}
\RequirePackage{caption}
\RequirePackage{chngcntr}
\RequirePackage{lipsum}
\RequirePackage{enumitem}

\RequirePackage{pdfpages}

\RequirePackage{xcolor}
\RequirePackage{microtype}
%\usemintedstyle{autumn}
\definecolor{DarkGrey}{gray}{0.075}
\definecolor{LightGrey}{gray}{0.925}

\RequirePackage{tocloft}
\renewcommand{\cftsecleader}{\hspace*{1em}\cftdotfill{0.95\cftdotsep}}
\renewcommand{\cftsubsecleader}{\hspace*{1em}\cftdotfill{0.95\cftdotsep}}
\renewcommand{\cftsubsubsecleader}{\hspace*{1em}\cftdotfill{0.95\cftdotsep}}
\renewcommand{\cftfigleader}{\hspace*{1em}\cftdotfill{0.95\cftdotsep}}
\renewcommand{\cfttableader}{\hspace*{1em}\cftdotfill{0.95\cftdotsep}}


\renewcommand{\arraystretch}{2}

\RequirePackage{array,tabularx}
\RequirePackage{longtable}

\hypersetup{
    colorlinks=true,
    linkcolor=black,
    filecolor=black,
    urlcolor=black,
    citecolor=black, % Establece el color del texto de los hipervínculos de citas en negro
    citebordercolor=white, % Establece el color del borde de los hipervínculos de citas en blanco (sin borde)
    linktoc=all
}

\newcommand{\membreteFont}[1]{\fontsize{12}{16}\selectfont\noindent #1}
\newcommand{\tituloPortadaFont}[1]{\fontsize{18}{22}\selectfont\noindent {\bfseries #1}}
\newcommand{\textoPortadaFont}[1]{\fontsize{12}{16}\selectfont\noindent #1}
\newcommand{\fechaPortadaFont}[1]{\fontsize{14}{18}\selectfont\noindent #1}
\newcommand{\leyendaFont}[1]{\fontsize{10}{12}\selectfont #1}
\newcommand{\palabrasClaveFont}[1]{\noindent #1}

\captionsetup{
  labelfont={bf,small},
  singlelinecheck=false,
  justification=centering
}

\counterwithin{figure}{section}
\counterwithin{table}{section}

% Configurar el formato de la etiqueta de la figura
\renewcommand{\thefigure}{\thesection.\arabic{figure}}

% Configurar el formato de la etiqueta de la tabla
\renewcommand{\thetable}{\thesection.\arabic{table}}

\newcommand{\membrete}[1]{%
  \membreteFont{#1}
}%

\newcommand{\tituloPortada}[1]{%
    \tituloPortadaFont{#1}\\
}%

\newcommand{\alumnos}[1]{%
    \textoPortadaFont{\textbf{#1}}
}%

\newcommand{\datoPortada}[2]{%
    \textoPortadaFont{#1: \textbf{#2}\\}
}%

\newcommand{\datosPortada}[1]{%
    #1
}%

\newcommand{\fechaPortada}[1]{%
    \fechaPortadaFont{#1}
}%

\newcommand{\palabrasClave}[1]{%
    \palabrasClaveFont{\textbf{Palabras clave: }\textit{#1}}
}%

\newcommand{\imagen}[3]{%
    \begin{figure}[htbp]
        \centering
        \includegraphics[width=0.9\textwidth]{#1}
        \caption{\leyendaFont{#2}}
        \label{fig:#3}
    \end{figure}
}

\NewDocumentCommand{\pdfExterno}{m m o}{%
    \phantomsection
    \addcontentsline{toc}{subsection}{#1}
    \IfValueTF{#3}{% 
        % * Si se especificaron números de página
        \includepdf[pages={#3}, pagecommand={\thispagestyle{plain}}]{#2}
    }{%
        % * Si no se especificaron números de página, incluir todas las páginas
        % ! Esto da error por el momento, hay que especificar el número de páginas
        % TODO: Arreglar esto
        \includepdf[pagecommand={\thispagestyle{plain}}]{#2}
    }%
}

\newcommand{\filaTabla}[1]{#1\\\hline}

\newcommand{\tabla}[4]{%
    \begin{table}[!htbp]
        \centering
        \caption{\leyendaFont{#1}}
        \label{tab:#2}
        {\scriptsize
            \begin{tabular}{#3}
                \hline #4
            \end{tabular}
        }
    \end{table}
}

\newlist{actividades}{enumerate}{1}
\newlist{hitos}{enumerate}{1}
\newlist{subactividades}{enumerate}{2}

\setlist[actividades]{label=\makebox[1.75em][r]{\textbf{\scriptsize{\arabic*}.-}},leftmargin=2.5em, align=left}
\setlist[hitos]{label=\makebox[1.5em][r]{\textbf{{\roman*}}},leftmargin=2.5em, align=left}
\setlist[subactividades]{label=\textbf{{\scriptsize\arabic{actividadesi}.\arabic*}.-},resume=actividades,leftmargin=1.5em, align=left}

\newcommand{\actividad}[2]{%
    {\scriptsize\bfseries(#1)}\hspace*{1em}{\small#2}
}

\newcommand{\hito}[2]{%
    {\scriptsize\bfseries(#1)}\hspace*{1em}{\small#2}
}

\setlength{\parskip}{1em} % Ajusta el espacio vertical entre párrafos a 1em
\setlength{\emergencystretch}{3em}
\hfuzz=10000pt
\vfuzz=10000pt
\hbadness=10000
\vbadness=10000
\AtBeginDocument{\sloppy}
\titlespacing{\section}{0pt}{0pt}{1em-\parskip}
\titlespacing{\subsection}{0pt}{0pt}{0.8em-\parskip}
\titlespacing{\subsubsection}{0pt}{0pt}{0.65em-\parskip}

\setlist{noitemsep} % Elimina el espacio vertical entre items de las listas